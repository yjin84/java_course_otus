package ru.otus.aop;

import ru.otus.aop.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyAwesomeIoC {

    private MyAwesomeIoC() {

    }

    static TestLoggingInterface createProxyFor(TestLoggingInterface instance) {
        InvocationHandler handler = new DemoInvocationHandler(instance);
        return (TestLoggingInterface) Proxy.newProxyInstance(MyAwesomeIoC.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLoggingInterface;
        private final Set<MetaMethod> loggingMethods;

        private final static class MetaMethod {
            private final String name;
            private final int parametersCount;

            private final Class<?>[] parametersTypes;

            private MetaMethod(String methodName, int parametersCount, Class<?>[] parametersTypes) {
                this.name = methodName;
                this.parametersCount = parametersCount;
                this.parametersTypes = parametersTypes;
            }

            public static MetaMethod valueOf(Method method) {
                return new MetaMethod(method.getName(), method.getParameterCount(), method.getParameterTypes());
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                MetaMethod method = (MetaMethod) o;

                return name.equals(method.name)
                        && parametersCount == method.parametersCount
                        && Arrays.equals(parametersTypes, method.parametersTypes);
            }

            @Override
            public int hashCode() {
                int result = name.hashCode();
                result = 31 * result + parametersCount;
                result = 31 * result + Arrays.hashCode(parametersTypes);
                return result;
            }
        }

        DemoInvocationHandler(TestLoggingInterface testLoggingInterface) {
            this.testLoggingInterface = testLoggingInterface;
            this.loggingMethods = detectLoggingMethods(testLoggingInterface);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            preprocessingMethod(method, args);

            return method.invoke(testLoggingInterface, args);
        }

        private void preprocessingMethod(Method method, Object[] args) {
            if (!loggingMethods.isEmpty()) {
                loggingParamsIfNeed(method, args);
            }
        }

        private void loggingParamsIfNeed(Method loggingMethod, Object[] args) {
            if (nothingToLog(loggingMethod, args)) {
                return;
            }

            var sb = new StringBuilder();
            sb.append("executed method:").append(loggingMethod.getName());
            int i = 0;
            for (var value : args) {
                sb.append(", param").append(i).append(":").append(value);
                i++;
            }
            System.out.println(sb);
        }

        private boolean nothingToLog(Method loggingMethod, Object[] args) {
            return loggingMethod == null
                    || args == null
                    || args.length == 0
                    || !loggingMethods.contains(MetaMethod.valueOf(loggingMethod));
        }

        private Set<MetaMethod> detectLoggingMethods(TestLoggingInterface testLoggingInterface) {
            var declaredMethods = testLoggingInterface.getClass().getDeclaredMethods();

            var detectedMethods = new HashSet<MetaMethod>(declaredMethods.length);

            for (var candidate : declaredMethods) {
                if (candidate.isAnnotationPresent(Log.class)) {
                    if (candidate.getParameterCount() < 1) {
                        System.out.println("Warning: method " + candidate + " annotated with Log annotation not have parameters and it will not logged");
                        continue;
                    }
                    detectedMethods.add(MetaMethod.valueOf(candidate));
                }
            }

            return detectedMethods;
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + testLoggingInterface +
                    '}';
        }
    }
}
