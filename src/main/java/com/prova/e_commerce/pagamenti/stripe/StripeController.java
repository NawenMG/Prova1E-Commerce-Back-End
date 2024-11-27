package com.prova.e_commerce.pagamenti.stripe;

import com.stripe.exception.StripeException;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi.PagamentiRepImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private PagamentiRepImp pagamentiRep;

    /**
     * Endpoint per creare un PaymentIntent.
     * @param amount L'importo del pagamento (in dollari o in base alla valuta scelta)
     * @return Client secret necessario per il frontend
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Double amount) {
        try {
            // Crea il PaymentIntent e ottieni il client secret
            String clientSecret = stripeService.createPaymentIntent(amount);
            return ResponseEntity.ok(clientSecret);
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore nella creazione del pagamento: " + e.getMessage());
        }
    }

    /**
     * Endpoint per eseguire il pagamento e aggiornare lo stato nel database.
     * @param paymentId L'ID del pagamento restituito da Stripe
     * @return Dettagli del pagamento salvato nel database
     */
    @PostMapping("/execute-payment")
    public ResponseEntity<?> executePayment(@RequestParam String paymentId) {
        try {
            // Esegui il pagamento e aggiorna il database
            stripeService.executePaymentAndSave(paymentId);

            // Recupera i dettagli del pagamento dal database
            Pagamenti pagamento = pagamentiRep.findByPaymentId(paymentId);
            if (pagamento != null) {
                return ResponseEntity.ok(pagamento);  // Restituisci il pagamento aggiornato
            } else {
                return ResponseEntity.badRequest().body("Pagamento non trovato.");
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Errore nell'esecuzione del pagamento: " + e.getMessage());
        }
    }
}
