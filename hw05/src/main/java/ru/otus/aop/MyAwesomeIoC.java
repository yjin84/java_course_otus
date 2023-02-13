package ru.otus.aop;

import ru.otus.aop.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAwesomeIoC {

    private MyAwesomeIoC() {

    }

    static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new DefaultTestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(MyAwesomeIoC.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLoggingInterface;
        private final List<Method> loggingMethods;

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
            if (loggingMethod == null || !needLogging(loggingMethod)) {
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

        private boolean needLogging(Method loggingMethod) {
            return loggingMethods.stream()
                    .anyMatch(
                            method ->
                                    method.getName().equals(loggingMethod.getName())
                                            && method.getParameterCount() == loggingMethod.getParameterCount()
                                            && Arrays.equals(method.getParameterTypes(), loggingMethod.getParameterTypes())
                    );
        }

        private List<Method> detectLoggingMethods(TestLoggingInterface testLoggingInterface) {
            var declaredMethods = testLoggingInterface.getClass().getDeclaredMethods();

            var detectedMethods = new ArrayList<Method>(declaredMethods.length);

            for (var candidate : declaredMethods) {
                if (candidate.isAnnotationPresent(Log.class)) {
                    detectedMethods.add(candidate);
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
