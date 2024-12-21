package com.prova.e_commerce.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.util.List;

@Service
public class OzoneService {

    private static final Logger logger = LoggerFactory.getLogger(OzoneService.class);
    private final S3Client s3Client;

    public OzoneService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // Crea un bucket se non esiste
    public void createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                logger.info("Bucket creato: {}", bucketName);
            } else {
                logger.warn("Il bucket '{}' esiste già.", bucketName);
            }
        } catch (S3Exception e) {
            logger.error("Errore durante la creazione del bucket '{}': {}", bucketName, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Creazione del bucket fallita", e);
        }
    }

    // Controlla se un bucket esiste
    public boolean bucketExists(String bucketName) {
        try {
            ListBucketsResponse buckets = s3Client.listBuckets();
            return buckets.buckets().stream().anyMatch(bucket -> bucket.name().equals(bucketName));
        } catch (S3Exception e) {
            logger.error("Errore durante la verifica dell'esistenza del bucket '{}': {}", bucketName, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Verifica dell'esistenza del bucket fallita", e);
        }
    }

    // Elenca tutti i bucket
    public List<String> listBuckets() {
        try {
            ListBucketsResponse buckets = s3Client.listBuckets();
            List<String> bucketNames = buckets.buckets().stream().map(Bucket::name).toList();
            bucketNames.forEach(bucket -> logger.info("Bucket trovato: {}", bucket));
            return bucketNames;
        } catch (S3Exception e) {
            logger.error("Errore durante l'elenco dei bucket: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Elenco dei bucket fallito", e);
        }
    }

    // Upload di un file
    public void uploadFile(String bucketName, String key, Path filePath, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(filePath));
            logger.info("File '{}' caricato nel bucket '{}' con key '{}'", filePath, bucketName, key);
        } catch (S3Exception e) {
            logger.error("Errore durante l'upload del file '{}': {}", filePath, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Upload fallito", e);
        }
    }

    // Download di un file
    public void downloadFile(String bucketName, String key, Path destinationPath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.getObject(getObjectRequest, destinationPath);
            logger.info("File '{}' scaricato dal bucket '{}' e salvato in '{}'", key, bucketName, destinationPath);
        } catch (S3Exception e) {
            logger.error("Errore durante il download del file '{}': {}", key, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Download fallito", e);
        }
    }

    // Elimina un file
    public void deleteFile(String bucketName, String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            logger.info("File '{}' eliminato dal bucket '{}'", key, bucketName);
        } catch (S3Exception e) {
            logger.error("Errore durante l'eliminazione del file '{}': {}", key, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Eliminazione fallita", e);
        }
    }

    // Elimina un bucket
    public void deleteBucket(String bucketName) {
        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.deleteBucket(deleteBucketRequest);
            logger.info("Bucket '{}' eliminato con successo", bucketName);
        } catch (S3Exception e) {
            logger.error("Errore durante l'eliminazione del bucket '{}': {}", bucketName, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Eliminazione del bucket fallita", e);
        }
    }
}
