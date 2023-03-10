package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorThrowingException implements Processor {
    private LocalDateTime currentTime;

    public ProcessorThrowingException(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public Message process(Message message) {
        if (currentTime != null && currentTime.getSecond() % 2 == 0) {
            throw new RuntimeException("test exception");
        }
        return message;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }
}
