package com.prova.e_commerce.pagamenti.stripe;

import com.google.api.client.util.Value;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.PagamentiService;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.exception.StripeException;
import com.stripe.Stripe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Autowired
    private PagamentiService pagamentiService;  // Inietta PagamentiService

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    // Inizializzazione della chiave segreta di Stripe
    public StripeService() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Metodo per creare un pagamento tramite Stripe.
     * @param total L'importo del pagamento
     * @param currency La valuta
     * @return Il client_secret necessario per completare il pagamento dal frontend
     */
    public String createPaymentIntent(Double total, String currency) throws StripeException {
        long amount = (long) (total * 100);  // Stripe richiede l'importo in centesimi
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setDescription("Pagamento per acquisto prodotti")
                .build();

        // Creazione del PaymentIntent tramite Stripe
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Creazione di un nuovo oggetto Pagamenti
        Pagamenti pagamento = new Pagamenti();
        pagamento.setPaymentsID(paymentIntent.getId());  // Imposta l'ID del pagamento
        pagamento.setType("Stripe");  // Metodo di pagamento (Stripe)
        pagamento.setStatus(false);  // Stato iniziale del pagamento (non completato)
        
        // Salvataggio del pagamento nel database tramite PagamentiService
        pagamentiService.inserisciPagamento(pagamento);

        // Restituisce il client_secret per completare il pagamento dal frontend
        return paymentIntent.getClientSecret();
    }

    /**
     * Metodo per eseguire il pagamento e aggiornare lo stato nel database.
     * @param paymentId L'ID del PaymentIntent
     */
    public void executePaymentAndSave(String paymentId) throws StripeException {
        // Recupera il PaymentIntent da Stripe
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);

        // Verifica che il pagamento sia stato completato con successo
        if (paymentIntent.getStatus().equals("succeeded")) {
            // Aggiorna lo stato del pagamento nel database tramite PagamentiService
            Pagamenti pagamento = new Pagamenti();
            pagamento.setPaymentsID(paymentId);
            pagamento.setStatus(true);  // Imposta lo stato come completato
            pagamentiService.aggiornaPagamento(paymentId, pagamento);  // Aggiorna nel database
        } else {
            // Gestisci il caso in cui il pagamento non sia riuscito
            // (puoi aggiungere logica di gestione degli errori qui)
            System.out.println("Errore: il pagamento non è stato completato.");
        }
    }
}
