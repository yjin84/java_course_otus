package ru.otus.processor;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorThrowingExceptionTest {
    @Test
    void testThrowingExceptionOnEvenSecond() {
        assertThrows(RuntimeException.class, () -> {
            var processor = new ProcessorThrowingException(ProcessorThrowingExceptionTest::getDateTimeEvenSecond);
            processor.process(null);

        });
    }

    private static LocalDateTime getDateTimeEvenSecond() {
        var currentTime = LocalDateTime.now();
        if (currentTime.getSecond() % 2 != 0) {
            currentTime = currentTime.plusSeconds(1);
        }
        return currentTime;
    }
}