package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.Objects;

public class ProcessorThrowingException implements Processor {
    private final LocalDateTime dateTime;

    public ProcessorThrowingException(DateTimeProvider dateTimeProvider) {
        this.dateTime = dateTimeProvider.getDateTime();
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime must be determine");
        }
    }

    @Override
    public Message process(Message message) {
        if (dateTime.getSecond() % 2 == 0) {
            throw new RuntimeException("test exception");
        }
        return message;
    }
}
