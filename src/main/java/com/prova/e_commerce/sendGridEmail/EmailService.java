package com.prova.e_commerce.sendGridEmail;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.default.sender}")
    private String defaultSenderEmail;

    /**
     * Invia un'email utilizzando SendGrid.
     *
     * @param toEmail  l'indirizzo email del destinatario
     * @param subject  il soggetto dell'email
     * @param content  il contenuto dell'email
     * @param isHtml   se true, invia il contenuto come HTML; altrimenti, come testo semplice
     */
    public void sendEmail(String toEmail, String subject, String content, boolean isHtml) {
        if (toEmail == null || toEmail.isEmpty()) {
            logger.error("Indirizzo email del destinatario non valido: {}", toEmail);
            throw new IllegalArgumentException("Indirizzo email del destinatario non può essere vuoto");
        }

        Email from = new Email(defaultSenderEmail);
        Email to = new Email(toEmail);
        Content contentObj = isHtml 
            ? new Content("text/html", content) 
            : new Content("text/plain", content);
        Mail mail = new Mail(from, subject, to, contentObj);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            logger.info("Preparazione per l'invio dell'email a: {}", toEmail);
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            int statusCode = response.getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                logger.info("Email inviata con successo. Status Code: {}", statusCode);
            } else {
                logger.error("Errore nell'invio dell'email. Status Code: {}, Body: {}", 
                             statusCode, response.getBody());
            }
        } catch (Exception e) {
            logger.error("Errore durante l'invio dell'email: {}", e.getMessage(), e);
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    /**
     * Metodo per verificare la validità della configurazione API.
     *
     * @return true se la configurazione è valida, altrimenti false.
     */
    public boolean isSendGridConfigured() {
        boolean isValid = sendGridApiKey != null && !sendGridApiKey.isEmpty() && defaultSenderEmail != null && !defaultSenderEmail.isEmpty();
        if (!isValid) {
            logger.warn("Configurazione SendGrid non valida: API Key o indirizzo mittente mancante.");
        }
        return isValid;
    }
}
