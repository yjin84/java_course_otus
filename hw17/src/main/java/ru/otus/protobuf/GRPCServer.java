package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RemoteSequenceServiceImpl;
import ru.otus.protobuf.service.SequenceServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var sequenceService = new SequenceServiceImpl();
        var remoteSequenceService = new RemoteSequenceServiceImpl(sequenceService);

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteSequenceService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
