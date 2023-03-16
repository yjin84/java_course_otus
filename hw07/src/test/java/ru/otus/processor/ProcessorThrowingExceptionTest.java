package ru.otus.processor;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorThrowingExceptionTest {

    @Test
    void testThrowingExceptionOnEvenSecond() {
        assertThrows(RuntimeException.class, () -> {
            var processor = new ProcessorThrowingException(() -> LocalDateTime.of(2024, 1, 1, 0, 0, 2));
            processor.process(null);

        });
    }
}