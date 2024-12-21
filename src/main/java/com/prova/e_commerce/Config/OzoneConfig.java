package com.prova.e_commerce.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class OzoneConfig {

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                "ozone-access-key", // Access key da application.yml
                "ozone-secret-key"  // Secret key da application.yml
        );

        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:9878")) // Endpoint S3 di Ozone
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_1) // Regione fittizia
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true) // Ozone usa l'accesso Path-Style
                        .build())
                .build();
    }
}
