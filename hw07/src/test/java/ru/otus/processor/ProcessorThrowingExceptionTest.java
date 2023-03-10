package ru.otus.processor;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorThrowingExceptionTest {
    @Test
    void testThrowingExceptionOnEvenSecond() {
        assertThrows(RuntimeException.class, () -> {
            var currentTime = LocalDateTime.now();
            var processor = new ProcessorThrowingException(currentTime);
            processor.process(null);
            processor.setCurrentTime(currentTime.plusSeconds(1));
            processor.process(null);
        });
    }
}