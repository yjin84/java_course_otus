package ru.otus.protobuf.service;

import java.util.List;

public interface SequenceService {
    List<Long> getSequence(Long firstNumber, Long lastNumber);
}
