package com.prova.e_commerce.pagamenti.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    /**
     * Endpoint per creare un pagamento.
     * @param request HttpServletRequest (opzionale per URL dinamici)
     * @return URL di approvazione PayPal o messaggio di errore
     */
    @GetMapping("/pay")
    public ResponseEntity<?> createPayment(@RequestParam Double total, @RequestParam String currency, HttpServletRequest request) {
    try {
        // Parametri di esempio per il pagamento
        String method = "paypal"; // Metodo di pagamento
        String intent = "sale"; // Intento
        String description = "Acquisto prodotti"; // Descrizione
        String cancelUrl = "http://localhost:8080/api/paypal/cancel"; // URL annullamento
        String successUrl = "http://localhost:8080/api/paypal/success"; // URL successo

        // Crea il pagamento tramite il servizio PayPal
        Payment payment = payPalService.createPayment(
                total, currency, method, intent, description, cancelUrl, successUrl);

        // Trova il link di approvazione PayPal
        for (var link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return ResponseEntity.ok(link.getHref()); // Restituisce l'URL di approvazione
            }
        }
    } catch (PayPalRESTException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body("Errore durante la creazione del pagamento: " + e.getMessage());
    }
    return ResponseEntity.badRequest().body("Errore durante il pagamento!");
}


    /**
     * Endpoint per gestire il successo del pagamento.
     * @param request HttpServletRequest (per ottenere parametri come paymentId e payerId)
     * @return Dettagli del pagamento salvato
     */
    @GetMapping("/success")
    public ResponseEntity<?> successPayment(HttpServletRequest request) {
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            // Esegui il pagamento e salva i dettagli nel database
            payPalService.executePaymentAndSave(paymentId, payerId);

            // Recupera il pagamento dal database per restituire i dettagli
            Pagamenti pagamenti = new Pagamenti();
            pagamenti.setPaymentsID(paymentId);
            pagamenti.setType("PayPal");
            pagamenti.setData(java.time.LocalDate.now());
            pagamenti.setStatus(true);
            //pagamenti.setTotal(new java.math.Long(50.00)); // Simulazione per esempio

            return ResponseEntity.ok(pagamenti);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore durante l'esecuzione del pagamento: " + e.getMessage());
        }
    }

    /**
     * Endpoint per gestire l'annullamento del pagamento.
     * @return Messaggio di annullamento
     */
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayment() {
        return ResponseEntity.ok("Pagamento annullato!");
    }
}
