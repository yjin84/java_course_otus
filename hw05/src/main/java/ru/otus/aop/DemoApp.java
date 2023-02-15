package ru.otus.aop;

public class DemoApp {
    public static void main(String[] args) {
        var testLogging = MyAwesomeIoC.createProxyFor(new DefaultTestLogging());
        testLogging.calculation();
        testLogging.calculation(10);
        testLogging.calculation(11,100);
        testLogging.calculation(101,111,"Hello, world!");
    }
}
