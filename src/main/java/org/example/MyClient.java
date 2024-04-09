package org.example;

import com.example.grpc.GreetingServiceGrpc;
import com.example.grpc.GreetingServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Iterator;

public class MyClient {
    public static void main(String[] args) {
        System.out.println("numbers MyClient is starting...");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        GreetingServiceOuterClass.NumberRequest request = GreetingServiceOuterClass.NumberRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();

        int currentValue = 0;
        int lastValueReceived = -1;
        int newValue = 0;

        Iterator<GreetingServiceOuterClass.NumberResponse> response = stub.greeting(request);
        while (currentValue < 50 && response.hasNext()) {
            GreetingServiceOuterClass.NumberResponse nextResponse = response.next();
            newValue = nextResponse.getValue();

            if (newValue == 1) {
                lastValueReceived = currentValue;
                currentValue++;
                System.out.println("currentValue: " + currentValue);

            } else if (newValue > 1) {
                System.out.println("new value: " + newValue);
                currentValue = currentValue + newValue + 1;
                System.out.println("currentValue: " + currentValue);
                lastValueReceived = currentValue;
                currentValue++;
                System.out.println("currentValue: " + currentValue);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        channel.shutdown();
    }
}
