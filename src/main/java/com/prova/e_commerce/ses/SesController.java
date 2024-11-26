package com.prova.e_commerce.ses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ses")
public class SesController {

    @Autowired
    private SesService sesService;

    /**
     * Endpoint per inviare un'email tramite Amazon SES
     * @param subject   Il soggetto dell'email
     * @param htmlBody  Il corpo dell'email in formato HTML
     * @return          Un messaggio di successo o errore
     */
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String subject,
                            @RequestParam String htmlBody) {
        try {
            sesService.sendEmail(subject, htmlBody);
            return "Email inviata con successo!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Errore nell'invio dell'email: " + e.getMessage();
        }
    }

    /**
     * Endpoint per inviare un'email con un template HTML predefinito
     * @param subject   Il soggetto dell'email
     * @return          Un messaggio di successo o errore
     */
    @PostMapping("/sendTemplateEmail")
    public String sendTemplateEmail(@RequestParam String subject) {
        try {
            // Invia l'email utilizzando un template HTML
            sesService.sendEmail(subject, null);  // htmlBody è null poiché il template è gestito dal servizio
            return "Email inviata con successo usando il template!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Errore nell'invio dell'email con template: " + e.getMessage();
        }
    }
}
