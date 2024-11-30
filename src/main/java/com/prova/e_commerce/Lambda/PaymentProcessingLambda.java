package com.prova.e_commerce.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.model.Message;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.Arrays;
import java.util.List;

public class PaymentProcessingLambda implements RequestHandler<List<Message>, Void> {

    private static final String PAYMENT_RESULT_QUEUE_URL = "https://sqs.eu-west-3.amazonaws.com/211125527920/PaymentResults"; // Seconda coda SQS per l'esito

    private SqsClient sqsClient = SqsClient.create();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Void handleRequest(List<Message> messages, Context context) {
        for (Message message : messages) {
            try {
                // Deserializza il messaggio JSON in un oggetto Pagamenti
                Pagamenti pagamento = objectMapper.readValue(message.getBody(), Pagamenti.class);

                // Log dei dati ricevuti
                System.out.println("Dati ricevuti per il pagamento: " + pagamento);

                // Elabora il pagamento (PayPal o Stripe)
                String result = processPayment(pagamento);

                // Crea un oggetto di esito del pagamento
                String paymentResult = "{\"userId\": \"" + pagamento.getPaymentsID() + "\", \"paymentMethod\": \"" + pagamento.getType() + "\", \"result\": \"" + result + "\"}";

                // Invia l'esito del pagamento alla seconda coda SQS
                sendMessageToSQS(paymentResult);

                // Log dell'esito
                System.out.println("Pagamento elaborato per utente " + pagamento.getPaymentsID() + ": " + result);

            } catch (Exception e) {
                // Log degli errori
                e.printStackTrace();
            }
        }
        return null;
    }

    // Funzione che simula l'elaborazione di un pagamento tramite PayPal o Stripe
    private String processPayment(Pagamenti pagamento) {
        switch (pagamento.getType().toLowerCase()) {
            case "paypal":
                // Logica per elaborare il pagamento tramite PayPal
                // (qui ci sarebbe la logica di chiamata alle API di PayPal)
                return processPaypalPayment(pagamento);
            case "stripe":
                // Logica per elaborare il pagamento tramite Stripe
                // (qui ci sarebbe la logica di chiamata alle API di Stripe)
                return processStripePayment(pagamento);
            default:
                return "Metodo di pagamento non supportato";
        }
    }



    private String processPaypalPayment(Pagamenti pagamento) {
        // Configura le credenziali PayPal
        String clientId = "YOUR_PAYPAL_CLIENT_ID";
        String clientSecret = "YOUR_PAYPAL_CLIENT_SECRET";
        String mode = "sandbox"; // Usa "live" in produzione
    
        // Crea un contesto API PayPal
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);
    
        try {
            // Crea un pagamento (questo è solo un esempio base)
            Amount amount = new Amount();
            amount.setTotal(pagamento.getTotal().toString()); // Importo totale
            amount.setCurrency(pagamento.getCurrency()); // Valuta (es. USD)
    
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Pagamento per l'ordine " + pagamento.getPaymentsID());
    
            // Payer (metodo di pagamento scelto, può essere PayPal o CreditCard, per esempio)
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal"); // "paypal" è il metodo di pagamento valido
    
            // Crea il pagamento con la transazione
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(Arrays.asList(transaction));
    
            // Esegui il pagamento
            Payment createdPayment = payment.create(apiContext);
    
            // Verifica che il pagamento sia stato completato
            if ("approved".equals(createdPayment.getState())) {
                return "Pagamento PayPal effettuato con successo per " + pagamento.getTotal() + " " + pagamento.getCurrency();
            } else {
                return "Pagamento PayPal fallito per l'ordine " + pagamento.getPaymentsID();
            }
    
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return "Errore durante il pagamento PayPal: " + e.getMessage();
        }
    }


    private String processStripePayment(Pagamenti pagamento) {
    // Configura le credenziali di Stripe
    Stripe.apiKey = "YOUR_STRIPE_SECRET_KEY";

    try {
        // Crea un pagamento con PaymentIntent
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        //.setAmount((long) (pagamento.getTotal() * 100)) // Stripe richiede l'importo in centesimi
                        .setCurrency(pagamento.getCurrency().toLowerCase()) // Converti la valuta in minuscolo
                        .setDescription("Pagamento per l'ordine " + pagamento.getPaymentsID())
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Verifica se il pagamento è stato completato
        if ("succeeded".equals(paymentIntent.getStatus())) {
            return "Pagamento Stripe effettuato con successo per " + pagamento.getTotal() + " " + pagamento.getCurrency();
        } else {
            return "Pagamento Stripe fallito per l'ordine " + pagamento.getPaymentsID();
        }

    } catch (StripeException e) {
        e.printStackTrace();
        return "Errore durante il pagamento Stripe: " + e.getMessage();
    }
}

    // Funzione per inviare un messaggio alla coda SQS con l'esito del pagamento
    private void sendMessageToSQS(String message) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(PAYMENT_RESULT_QUEUE_URL)
                .messageBody(message)
                .build();

        // Invia il messaggio alla coda SQS
        SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);

        // Log dell'esito
        System.out.println("Esito inviato alla coda SQS: " + sendMsgResponse.messageId());
    }
}
