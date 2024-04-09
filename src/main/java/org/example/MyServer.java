package org.example;

import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.GreetingServiceOuterClass;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class MyServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new NumberSequenceServiceImpl())
                .build();

        server.start();
        System.out.println("Server started...");

        server.awaitTermination();
    }

    static class NumberSequenceServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

        public void greeting(GreetingServiceOuterClass.NumberRequest request, StreamObserver<GreetingServiceOuterClass.NumberResponse> responseObserver) {
            int firstValue = request.getFirstValue();
            int lastValue = request.getLastValue();

            for (int i = firstValue + 1; i <= lastValue; i++) {
                GreetingServiceOuterClass.NumberResponse response = GreetingServiceOuterClass.NumberResponse.newBuilder()
                        .setValue(i)
                        .build();
                responseObserver.onNext(response);
                try {
                    Thread.sleep(2000); // Раз в две секунды генерируем новое значение
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            responseObserver.onCompleted();
        }
    }
}
