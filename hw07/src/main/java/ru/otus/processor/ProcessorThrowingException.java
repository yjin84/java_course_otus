package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowingException implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowingException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var dateTime = dateTimeProvider.getDateTime();
        if (dateTime != null && dateTime.getSecond() % 2 == 0) {
            throw new RuntimeException("test exception");
        }
        return message;
    }
}
