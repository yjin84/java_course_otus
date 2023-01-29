package ru.otus.test;

import ru.otus.simpletesttool.annotations.After;
import ru.otus.simpletesttool.annotations.Before;
import ru.otus.simpletesttool.annotations.Test;

import java.util.UUID;

public class MyAwesomeTest {
    private String testName;

    @Before
    void before() {
        testName = "AwesomeTest-" + UUID.randomUUID();
    }

    @Test
    void test() {
        System.out.println(testName);
    }

    @Test
    void test1() {
        System.out.println(testName + "-2");
        throw new RuntimeException("Test 2 failed");
    }

    @Test
    void test2() {
        System.out.println(testName + "-3");
    }

    @After
    void after() {
        System.out.println(testName + " done.");
    }
}
