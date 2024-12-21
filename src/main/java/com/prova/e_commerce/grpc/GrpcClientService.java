/* package com.prova.e_commerce.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.grpc.InputServiceGrpc;
import com.example.grpc.InputRequest;
import com.example.grpc.InputResponse;

import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {
    public void sendInput(String inputData, int inputId) {
        // Crea il canale gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        // Crea il client
        InputServiceGrpc.InputServiceBlockingStub stub = InputServiceGrpc.newBlockingStub(channel);

        // Prepara e invia la richiesta
        InputRequest request = InputRequest.newBuilder()
                .setInputData(inputData)
                .setInputId(inputId)
                .build();

        InputResponse response = stub.sendInput(request);

        // Stampa la risposta
        System.out.println("Response: " + response.getMessage());
        System.out.println("Success: " + response.getSuccess());

        // Chiude il canale
        channel.shutdown();
    }
}
 */