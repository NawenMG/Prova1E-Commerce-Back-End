package com.prova.e_commerce.sns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sns")
public class SnsController {

    private final SnsService snsService;

    @Autowired
    public SnsController(SnsService snsService) {
        this.snsService = snsService;
    }

    /**
     * Endpoint per inviare un messaggio a tutti gli iscritti al topic SNS
     * @param message   Il messaggio da inviare
     * @param protocol  Il protocollo (email, sms, http)
     * @return          L'ID del messaggio pubblicato o un messaggio di errore
     */
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam String message,
                              @RequestParam String protocol) {
        return snsService.sendMessageWithProtocol(message, protocol);
    }

    /**
     * Endpoint per iscrivere un endpoint (email, sms, http) a un topic SNS
     * @param protocol  Il protocollo di iscrizione (email, sms, http)
     * @param endpoint  L'endpoint da iscrivere (ad esempio un indirizzo email o un numero di telefono)
     * @return          L'ARN della sottoscrizione o un messaggio di errore
     */
    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String protocol,
                            @RequestParam String endpoint) {
        return snsService.subscribeToTopic(protocol, endpoint);
    }
}
