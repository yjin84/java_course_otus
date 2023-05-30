package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NextNumberMessage;
import ru.otus.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.generated.SequenceRangeRequest;

import java.util.List;

public class RemoteSequenceServiceImpl extends RemoteSequenceServiceGrpc.RemoteSequenceServiceImplBase {
    private final SequenceService sequenceService;

    public RemoteSequenceServiceImpl(SequenceService realDBService) {
        this.sequenceService = realDBService;
    }

    @Override
    public void getSequence(SequenceRangeRequest request, StreamObserver<NextNumberMessage> responseObserver) {
        List<Long> allNumbers = sequenceService.getSequence(request.getFirstNumber(), request.getLastNumber());

        allNumbers.forEach(u -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(createSequenceRequest(u));
        });
        responseObserver.onCompleted();
    }

    private NextNumberMessage createSequenceRequest(Long nextNumber) {
        return NextNumberMessage.newBuilder()
                .setNextNumber(nextNumber)
                .build();
    }
}
