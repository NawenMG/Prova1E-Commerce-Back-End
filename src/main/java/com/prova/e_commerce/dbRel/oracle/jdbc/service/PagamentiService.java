package com.prova.e_commerce.dbRel.oracle.jdbc.service;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce.PagamentiRep;
import com.prova.e_commerce.JsonConvert.JsonUtil;

import software.amazon.awssdk.services.sqs.SqsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;


import java.util.List;

@Service
public class PagamentiService {

    @Autowired
    private PagamentiRep pagamentiRep;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka producer

    private static final String KAFKA_TOPIC_AGGIUNGI = "pagamenti-topic-aggiungi"; 
    private static final String KAFKA_TOPIC_AGGIORNA = "pagamenti-topic-aggiorna";


    @Autowired
    private SqsClient sqsClient;

    private static final String QUEUE_URL = "https://sqs.eu-west-3.amazonaws.com/211125527920/InputPagamenti"; // URL della tua coda

    public void sendMessageToSQS(String message){
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
        .queueUrl(QUEUE_URL)
        .messageBody(message)  // Il messaggio in formato JSON
        .build();

      // Invia il messaggio alla coda
      SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);

     // Log del risultato per il controllo
    System.out.println("Messaggio inviato alla coda SQS: " + sendMsgResponse.messageId());
   }



    /**
     * Metodo per eseguire una query avanzata sui pagamenti in base a parametri dinamici.
     * Utilizza Caffeine per 10 minuti e Redis per 30 minuti.
     */
    @Cacheable(value = {"caffeine", "redis"}, key = "#paramQuery.toString() + #pagamenti.toString()")
    public List<Pagamenti> queryPagamenti(ParamQuery paramQuery, Pagamenti pagamenti) {
        return pagamentiRep.query(paramQuery, pagamenti);
    }

    /**
     * Metodo per inserire un nuovo pagamento.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'inserimento di un nuovo pagamento.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String inserisciPagamento(Pagamenti pagamenti) {
        String result = pagamentiRep.insertPayment(pagamenti);
        
        // Invia un messaggio Kafka che indica l'inserimento di un nuovo pagamento
        kafkaTemplate.send(KAFKA_TOPIC_AGGIUNGI, "Nuovo pagamento inserito: " + pagamenti.getPaymentsID());

        String paymentData = JsonUtil.convertToJson(pagamenti);
        sendMessageToSQS(paymentData);

        return result;

    }

    /**
     * Metodo per aggiornare un pagamento esistente in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'aggiornamento di un pagamento.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String aggiornaPagamento(String paymentID, Pagamenti pagamenti) {
        String result = pagamentiRep.updatePayment(paymentID, pagamenti);
        
        // Invia un messaggio Kafka che indica l'aggiornamento del pagamento
        kafkaTemplate.send(KAFKA_TOPIC_AGGIORNA, "Pagamento aggiornato: " + paymentID);
        
        return result;
    }

    /**
     * Metodo per eliminare un pagamento in base all'ID.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare l'eliminazione di un pagamento.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String eliminaPagamento(String paymentID) {
        String result = pagamentiRep.deletePayment(paymentID);
        
        return result;
    }

    /**
     * Metodo per generare un numero specificato di pagamenti con dati casuali.
     * Rimuove la cache per garantire che i dati siano aggiornati.
     * Produce un messaggio Kafka per notificare il salvataggio di pagamenti casuali.
     */
    @CacheEvict(value = {"caffeine", "redis"}, allEntries = true)
    public String salvaPagamentiCasuali(int numero) {
        String result = pagamentiRep.saveAll(numero);
        
        return result;
    }
}
