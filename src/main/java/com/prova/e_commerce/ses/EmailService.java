package com.prova.e_commerce.ses;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.influxdb.client.service.UsersService;

import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.ses.SesClient;

@Service
public class EmailService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private SesClient sesClient;
    
    private final String SENDER = "mariogranato2001@outlook.com";  
    private final String RECIPIENT = "emaildiDestinazione@icloud.it"; //Usa email trap come destinatario
    //private final String RECIPIENT = usersService.query; USA QUESTO QUANDO IMPLEMENTI IL SERVICE DI USERS
    
    // Metodo per leggere il contenuto HTML da un file
    public String readHtmlTemplate(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public void sendEmail(String subject, String htmlBody) throws Exception {
         // Leggi il contenuto del template HTML
         htmlBody = readHtmlTemplate("src/main/resources/emailTemplate.html");

         // Personalizza il corpo dell'email (facoltativo, se hai variabili nel template)
         htmlBody = htmlBody.replace("{{recipientName}}", "John Doe");  // Sostituisci variabili, se necessario


        try {
             // Crea il messaggio email con corpo HTML
             SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
             .source(SENDER)  // Email mittente
             .destination(Destination.builder().toAddresses(RECIPIENT).build())  // Destinatario
             .message(Message.builder()
                     .subject(Content.builder().data(subject).build())  // Soggetto
                     .body(Body.builder()
                             .html(Content.builder().data(htmlBody).build())  // Corpo in HTML
                             .build())
                     .build())
             .build();

            // Invia l'email
            sesClient.sendEmail(sendEmailRequest);
            System.out.println("Email inviata con successo!");

        } catch (Exception e) {
            System.err.println("Errore nell'invio dell'email: " + e.getMessage());
        }
    }


}
