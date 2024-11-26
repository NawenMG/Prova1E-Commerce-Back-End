package com.prova.e_commerce.sns;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class SnsService {

    private final SnsClient snsClient;

    @Value("${aws.sns.topicArn}")
    private String topicArn;

    public SnsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    // Metodo per caricare il template HTML dal file
    private String readHtmlTemplate(String templatePath) throws IOException {
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(templatePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }
        return htmlContent.toString();
    }

    // Metodo per inviare un messaggio a tutti gli iscritti
    public String sendMessageWithProtocol(String message, String protocol) {
        try {
            // Decidi come gestire il messaggio in base al protocollo
            String formattedMessage = null;
            String subject = "Notifica da SNS";

            // Gestire il tipo di messaggio in base al protocollo
            switch (protocol.toLowerCase()) {
                case "email":
                    formattedMessage = sendEmailWithTemplate(message); // Email personalizzata
                    break;
                case "sms":
                    formattedMessage = sendSmsMessage(message); // SMS semplice
                    break;
                case "http":
                    formattedMessage = sendHttpPostNotification(message); // HTTP
                    break;
                default:
                    throw new IllegalArgumentException("Protocollo non supportato.");
            }

            // Pubblica il messaggio sul topic SNS
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(formattedMessage)
                    .subject(subject)
                    .messageStructure("string")
                    .build();

            PublishResponse result = snsClient.publish(request);
            return result.messageId();
        } catch (Exception e) {
            e.printStackTrace();
            return "Errore durante l'invio del messaggio.";
        }
    }

    // Metodo per inviare un'email con template HTML personalizzato
    private String sendEmailWithTemplate(String messaggio) throws IOException {
        // Carica il template HTML
        String htmlBody = readHtmlTemplate("src/main/resources/emailTemplate.html");

        // Sostituisci le variabili nel template
        htmlBody = htmlBody.replace("{{messaggio}}", messaggio);

        // Restituisci il corpo dell'email personalizzato
        return htmlBody;
    }

    // Metodo per inviare un SMS
    private String sendSmsMessage(String messaggio) {
        // Prepara il messaggio SMS
        return "Ciao! Hai una nuova notifica. Controlla il tuo account per maggiori dettagli.";
    }

    // Metodo per inviare una notifica HTTP
    private String sendHttpPostNotification(String recipientEndpoint) {
        // Puoi configurare una richiesta HTTP per inviare i dati al tuo endpoint.
        // In questo esempio, restituiremo solo una stringa che rappresenta la notifica HTTP.
        return "Notifica inviata all'endpoint HTTP: " + recipientEndpoint;
    }

    // Metodo per iscrivere un endpoint al topic SNS
    public String subscribeToTopic(String protocol, String endpoint) {
        // Decidi cosa fare in base al protocollo
        String message = null;
        switch (protocol.toLowerCase()) {
            case "email":
                message = "Iscrizione via Email: " + endpoint;
                break;
            case "sms":
                message = "Iscrizione via SMS: " + endpoint;
                break;
            case "http":
                message = "Iscrizione via HTTP: " + endpoint;
                break;
            default:
                throw new IllegalArgumentException("Protocollo non supportato.");
        }

        // Iscrivi l'endpoint al topic SNS
        SubscribeRequest request = SubscribeRequest.builder()
                .topicArn(topicArn)
                .protocol(protocol)
                .endpoint(endpoint)
                .build();

        SubscribeResponse response = snsClient.subscribe(request);
        return response.subscriptionArn();
    }
}
