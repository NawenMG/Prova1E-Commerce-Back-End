package com.prova.e_commerce.pagamenti.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.classi.PagamentiRepImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext; //Per l'api paypal

    @Autowired
    private PagamentiRepImp pagamentiRep; //Chiama il repository di Pagamenti

    public Payment createPayment( //Configurazione del pagamento, da paypal a satispay
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {
        
        Amount amount = new Amount(); //Definizione dell'importo
        amount.setCurrency(currency); //Settiamo la valuta
        amount.setTotal(String.format("%.2f", total)); //Settiamo il totale con il formato a due cifre

        Transaction transaction = new Transaction(); //La transizione contiene la descrizione e l'importo
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>(); 
        transactions.add(transaction); 

        Payer payer = new Payer(); //Configurazione del metodo di pagamento
        payer.setPaymentMethod(method);

        Payment payment = new Payment(); // Configura l'intento del pagamento (es., "sale" per transazioni immediate) e associa le transazioni.
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls(); // Aggiunge gli URL di reindirizzamento per successo e annullamento.
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);


        return payment.create(apiContext);
    }


    public void executePaymentAndSave(String paymentId, String payerId) throws PayPalRESTException {
        // Esegui il pagamento PayPal
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        // Salva i dettagli nella tua entità `Pagamenti`
        Pagamenti pagamenti = new Pagamenti();
        pagamenti.setPaymentsID(executedPayment.getId());
        pagamenti.setType("PayPal");
        pagamenti.setData(LocalDate.now());
        pagamenti.setStatus(true); // Imposta come completato
        //pagamenti.setTotal(new BigDecimal(executedPayment.getTransactions().get(0).getAmount().getTotal()));

        pagamentiRep.insertPayment(pagamenti);
    }
}
