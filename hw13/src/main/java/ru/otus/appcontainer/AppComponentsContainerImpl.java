package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.DoubleComponentException;
import ru.otus.appcontainer.exceptions.NotFoundComponentException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        if (configClasses == null) {
            throw new IllegalArgumentException("At least one class must be set");
        }
        processConfigs(Set.of(configClasses));
    }

    public AppComponentsContainerImpl(String configPackage) {
        if (configPackage == null || configPackage.isEmpty()) {
            throw new IllegalArgumentException("Config package must be not empty");
        }

        var classes = new Reflections(configPackage, Scanners.TypesAnnotated).getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        processConfigs(classes);
    }

    private void processConfigs(Set<Class<?>> classes) {
        List<MetaConfig> metaConfigs = new ArrayList<>(classes.size());

        for (Class<?> clazz : classes) {
            checkConfigClass(clazz);
            var order = clazz.getAnnotation(AppComponentsContainerConfig.class).order();
            metaConfigs.add(new MetaConfig(order, clazz));
        }

        metaConfigs.sort(Comparator.comparingInt(MetaConfig::getOrder));

        for (MetaConfig metaConfig : metaConfigs) {
            processConfig(metaConfig.getClazz());
        }
    }

    private void processConfig(Class<?> configClass) {
        try {
            var configInstance = configClass.getDeclaredConstructor().newInstance();

            var methods = configClass.getMethods();

            var metaComponents = createMetaComponents(methods);

            metaComponents.sort(Comparator.comparingInt(MetaComponent::getOrder));

            processComponents(configInstance, metaComponents);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processComponents(Object configInstance, List<MetaComponent> metaComponents) throws IllegalAccessException, InvocationTargetException {
        for (MetaComponent metaComponent : metaComponents) {
            Object component;

            var parameters = metaComponent.getParameters();

            if (parameters == null) {
                component = metaComponent.getMethod().invoke(configInstance);
            } else {
                List<Object> dependencies = new ArrayList<>(parameters.length);

                for (var parameter : parameters) {
                    dependencies.add(getAppComponent(parameter.getType()));
                }

                component = metaComponent.getMethod().invoke(configInstance, dependencies.toArray());
            }

            addComponentToContext(metaComponent.getComponentName(), component);
        }
    }

    private void addComponentToContext(String componentName, Object component) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new DoubleComponentException(String.format("Given component loaded already: %s", componentName));
        }

        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private List<MetaComponent> createMetaComponents(Method[] methods) {
        List<MetaComponent> metaComponents = new ArrayList<>(methods.length);

        for (var method : methods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                var appComponentAnnotation = method.getAnnotation(AppComponent.class);

                var order = appComponentAnnotation.order();
                var name = appComponentAnnotation.name();

                var parameters = method.getParameters();

                var metaComponent = new MetaComponent(order, name, method, parameters);

                metaComponents.add(metaComponent);
            }
        }

        return metaComponents;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return getComponentByClass(componentClass);
    }

    private <C> C getComponentByClass(Class<C> componentClass) {
        var results = appComponents.stream()
                .filter(item -> componentClass.isAssignableFrom(item.getClass()))
                .toList();
        if (results.size() == 0) {
            throw new NotFoundComponentException(String.format("Not found component %s", componentClass));
        }

        if (results.size() > 1) {
            throw new DoubleComponentException(String.format("Found more than 1 component: %s", componentClass));
        }

        return (C) results.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new NotFoundComponentException(String.format("Not found component %s", componentName));
        }
        return (C) component;
    }

    private final class MetaConfig {
        private final int order;

        private final Class<?> clazz;

        private MetaConfig(int order, Class<?> clazz) {
            this.order = order;
            this.clazz = clazz;
        }

        public int getOrder() {
            return order;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }

    private final class MetaComponent {
        private final int order;

        private final String componentName;

        private final Method method;

        private final Parameter[] parameters;

        private MetaComponent(int order, String componentName, Method method, Parameter[] parameters) {
            this.order = order;
            this.componentName = componentName;
            this.method = method;
            this.parameters = parameters;
        }


        public String getComponentName() {
            return componentName;
        }

        public Method getMethod() {
            return method;
        }

        public Parameter[] getParameters() {
            return parameters;
        }

        public int getOrder() {
            return order;
        }
    }
}
