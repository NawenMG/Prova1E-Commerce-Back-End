package com.prova.e_commerce.twilioSMS;

import com.prova.e_commerce.Config.TwilioConfig;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private TwilioConfig twilioConfig;

    /**
     * Metodo per inviare un SMS tramite Twilio.
     *
     * @param toPhoneNumber il numero di telefono del destinatario in formato internazionale (+CCNNNNNNNNNNN)
     * @param messageBody   il contenuto del messaggio SMS
     * @throws IllegalArgumentException se il numero di telefono non è valido
     * @throws RuntimeException         per errori API Twilio o altri errori imprevisti
     */
    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            // Validazione del numero di telefono
            validatePhoneNumber(toPhoneNumber);

            // Validazione del corpo del messaggio
            if (messageBody == null || messageBody.isEmpty()) {
                throw new IllegalArgumentException("Il corpo del messaggio non può essere vuoto.");
            }

            // Creazione e invio del messaggio
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber), // Numero del destinatario
                    new PhoneNumber(twilioConfig.getPhoneNumber()), // Numero Twilio
                    messageBody) // Testo del messaggio
                    .create();

            logger.info("SMS inviato con successo. SID: {}", message.getSid());
        } catch (IllegalArgumentException e) {
            logger.error("Errore di validazione: {}", e.getMessage());
            throw e; // Rilancia l'eccezione per il chiamante
        } catch (ApiException e) {
            logger.error("Errore API Twilio: {}, Codice: {}, Status: {}",
                    e.getMessage(), e.getCode(), e.getStatusCode());
            throw new RuntimeException("Errore Twilio API", e);
        } catch (Exception e) {
            logger.error("Errore imprevisto durante l'invio dell'SMS: {}", e.getMessage());
            throw new RuntimeException("Errore sconosciuto", e);
        }
    }

    /**
     * Metodo per validare il numero di telefono.
     *
     * @param phoneNumber il numero di telefono da validare
     * @throws IllegalArgumentException se il numero di telefono non è valido
     */
    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Numero di telefono non valido: " + phoneNumber);
        }
        logger.debug("Numero di telefono validato con successo: {}", phoneNumber);
    }
}
