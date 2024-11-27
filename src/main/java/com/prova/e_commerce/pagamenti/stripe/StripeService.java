package com.prova.e_commerce.pagamenti.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi.PagamentiRepImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private PagamentiRepImp pagamentiRep;

    public StripeService() {
        // Impostiamo la chiave segreta di Stripe per l'autenticazione
        Stripe.apiKey = stripeSecretKey;
    }

    // Metodo per creare un PaymentIntent e salvarlo nel database
    public String createPaymentIntent(Double amount) throws StripeException {
        // Creazione di un PaymentIntent tramite l'API di Stripe
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(Math.round(amount * 100)) // Convertiamo in centesimi
                        .setCurrency("usd") // Puoi cambiare la valuta a seconda delle tue esigenze
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Crea un'istanza di Pagamenti per salvare i dettagli nel DB
        Pagamenti pagamento = new Pagamenti();
        pagamento.setPaymentsID(paymentIntent.getId());
        pagamento.setType("Stripe");
        pagamento.setData(LocalDate.now());  // Aggiungi la data attuale
        pagamento.setStatus(false);  // Status iniziale come "false" (ancora non completato)
        pagamento.setTotal(BigDecimal.valueOf(amount)); // Impostiamo l'importo del pagamento

        // Salviamo il pagamento nel database
        pagamentiRep.insertPayment(pagamento);

        // Restituiamo il client secret, che è necessario per il frontend
        return paymentIntent.getClientSecret();
    }

    // Metodo per eseguire il pagamento e aggiornare lo stato nel DB
    public void executePaymentAndSave(String paymentId) throws StripeException {
        // Recuperiamo il PaymentIntent tramite l'ID
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);

        // Verifichiamo lo stato del pagamento
        if (paymentIntent.getStatus().equals("succeeded")) {
            // Aggiorniamo il pagamento nel database come completato
            Pagamenti pagamento = pagamentiRep.findByPaymentId(paymentId);
            if (pagamento != null) {
                pagamento.setStatus(true);  // Impostiamo lo stato a "true" per completato
                pagamentiRep.updatePayment(paymentId, pagamento);  // Salviamo le modifiche nel DB
            }
        }
    }
}
