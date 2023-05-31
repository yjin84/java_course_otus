package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NextNumberMessage;
import ru.otus.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.protobuf.generated.SequenceRangeRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

public class GRPCClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var rangeRequest = SequenceRangeRequest.newBuilder()
                .setFirstNumber(0)
                .setLastNumber(30)
                .build();

        final AtomicLong lastNumberFromServer = new AtomicLong(0);

        var latch = new CountDownLatch(1);

        var newStub = RemoteSequenceServiceGrpc.newStub(channel);
        newStub.getSequence(rangeRequest, new StreamObserver<>() {
            @Override
            public void onNext(NextNumberMessage nextNumberMessage) {
                long nextNumber = nextNumberMessage.getNextNumber();
                System.out.println("nextNumber from server: " + nextNumber);
                lastNumberFromServer.set(nextNumber);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\n\nЯ все!");
                latch.countDown();
            }
        });

        var thread = new Thread(() -> {
            try {
                long currentValue = 0;
                long lastNumber = 0;
                long incNumber = 0;
                for (int i = 0; i < 50; i++) {
                    long localLastNumber = lastNumberFromServer.get();

                    if (lastNumber != localLastNumber) {
                        lastNumber = localLastNumber;
                        incNumber = lastNumber;
                    } else {
                        incNumber = 0;
                    }

                    currentValue = currentValue + incNumber + 1;
                    System.out.println("current value: " + currentValue);
                    sleep(1000);
                }
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        });

        thread.start();

        latch.await();

        channel.shutdown();
    }
}
