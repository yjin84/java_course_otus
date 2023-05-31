package ru.otus.protobuf.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SequenceServiceImpl implements SequenceService {
    @Override
    public List<Long> getSequence(Long firstNumber, Long lastNumber) {
        return LongStream.rangeClosed(firstNumber, lastNumber).boxed().collect(Collectors.toList());
    }
}
