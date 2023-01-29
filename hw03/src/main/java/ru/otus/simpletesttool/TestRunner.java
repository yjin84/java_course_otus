package ru.otus.simpletesttool;

import ru.otus.simpletesttool.annotations.After;
import ru.otus.simpletesttool.annotations.Before;
import ru.otus.simpletesttool.annotations.Test;
import ru.otus.utilities.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        try {
            Class<?> testClass = parseArgs(args);

            runTests(testClass);
        } catch (Exception ex) {
            log(ex.getMessage());
        }
    }

    private static Class<?> parseArgs(String[] args) {
        if (args.length == 0) {
            log("Not specified test class");
            exit();
        }

        try {
            return Class.forName(args[0]);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void runTests(Class<?> testClass) {
        Method[] methods = getMethods(testClass);

        List<Method> beforeMethods = new ArrayList<>(methods.length);
        List<Method> afterMethods = new ArrayList<>(methods.length);
        List<Method> testMethods = new ArrayList<>(methods.length);

        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        if (testMethods.isEmpty()) {
            log("Not detected tests in class " + testClass.getName());
            exit();
        }

        int successTests = 0;
        int failTests = 0;

        for (Method method : testMethods) {
            if (runTest(testClass, beforeMethods, method, afterMethods)) {
                successTests += 1;
            } else {
                failTests += 1;
            }
        }

        log("Success tests " + successTests + ", fail tests " + failTests + " from " + testMethods.size());
    }

    private static boolean runTest(Class<?> testClass, List<Method> beforeMethods, Method testMethod, List<Method> afterMethods) {
        Object instance = ReflectionHelper.instantiate(testClass);

        try {
            log("Calling before methods");
            callMethods(instance, beforeMethods);
        } catch (Exception ex) {
            log("Error occurred when call before methods: " + ex.getMessage());
            return false;
        }

        try {
            log("Calling test method");
            callMethod(instance, testMethod);
            return true;
        } catch (Exception ex) {
            log("Error occurred when call test method: " + ex.getMessage());
            return false;
        } finally {
            try {
                log("Calling after methods");
                callMethods(instance, afterMethods);
            } catch (Exception ex) {
                log("Error occurred when call test methods: " + ex.getMessage());
            }
            System.out.println("===");
        }
    }

    private static void callMethods(Object instance, List<Method> beforeMethods) {
        for (Method method : beforeMethods) {
            callMethod(instance, method);
        }
    }

    private static void callMethod(Object instance, Method method) {
        if (method != null) {
            try {
                method.setAccessible(true);
                method.invoke(instance);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static Method[] getMethods(Class<?> testClass) {
        Method[] methods = testClass.getDeclaredMethods();

        if (methods.length == 0) {
            log("Not detected methods in class with name " + testClass.getName());
            exit();
        }
        return methods;
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void exit() {
        System.exit(0);
    }
}
