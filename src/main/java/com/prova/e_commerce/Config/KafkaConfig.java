package com.prova.e_commerce.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic ordiniTopicSave() {
        return TopicBuilder.name("ordini-topic-save")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ordiniTopicUpdate() {
        return TopicBuilder.name("ordini-topic-update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic segnalazioniTopicSave() {
        return TopicBuilder.name("segnalazioni-topic-save")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic segnalazioniTopicUpdate() {
        return TopicBuilder.name("segnalazioni-topic-update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic transizioniTopicSave() {
        return TopicBuilder.name("transizioni-topic-save")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic transizioniTopicUpdate() {
        return TopicBuilder.name("transizioni-topic-update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic recensioniTopicSave() {
        return TopicBuilder.name("recensioni-topic-save")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic recensioniTopicUpdate() {
        return TopicBuilder.name("recensioni-topic-update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic schedaProdottiTopicSave() {
        return TopicBuilder.name("scheda-prodotti-topic-save")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic schedaProdottiTopicUpdate() {
        return TopicBuilder.name("scheda-prodotti-topic-update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiProdottiVisitaTopic() {
        return TopicBuilder.name("eventi-prodotti-topic-visita")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiProdottiAcquistaTopic() {
        return TopicBuilder.name("eventi-prodotti-topic-acquista")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiAppartenenzaCategoriaTopic() {
        return TopicBuilder.name("eventi-appartenenza-categoria")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiProvenienzaUtentiTopic() {
        return TopicBuilder.name("eventi-provenienza-utenti")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiCronologiaAggiungiTopic() {
        return TopicBuilder.name("eventi-cronologia-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiCarrelloAggiungiTopic() {
        return TopicBuilder.name("eventi-carrello-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiWishlistAggiungiTopic() {
        return TopicBuilder.name("eventi-wishlist-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiUtenteAggiungiTopic() {
        return TopicBuilder.name("eventi-utente-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiUtenteAggiornaTopic() {
        return TopicBuilder.name("eventi-utente-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiCategoriaAggiungiTopic() {
        return TopicBuilder.name("eventi-categoria-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventiCategoriaAggiornaTopic() {
        return TopicBuilder.name("eventi-categoria-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ordiniAggiungiTopic() {
        return TopicBuilder.name("ordini-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ordiniAggiornaTopic() {
        return TopicBuilder.name("ordini-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pagamentiAggiungiTopic() {
        return TopicBuilder.name("pagamenti-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pagamentiAggiornaTopic() {
        return TopicBuilder.name("pagamenti-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic resiAggiungiTopic() {
        return TopicBuilder.name("resi-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic resiAggiornaTopic() {
        return TopicBuilder.name("resi-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shippingStatusAggiungiTopic() {
        return TopicBuilder.name("shipping-status-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shippingStatusAggiornaTopic() {
        return TopicBuilder.name("shipping-status-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic salesMonitoringAggiungiTopic() {
        return TopicBuilder.name("sales-monitoring-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic salesMonitoringAggiornaTopic() {
        return TopicBuilder.name("sales-monitoring-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic serverResponseAggiungiTopic() {
        return TopicBuilder.name("server-response-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic serverResponseAggiornaTopic() {
        return TopicBuilder.name("server-response-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic trafficAnalysisAggiungiTopic() {
        return TopicBuilder.name("traffic-analysis-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userAnalysisAggiungiTopic() {
        return TopicBuilder.name("user-analysis-topic-aggiungi")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userAnalysisAggiornaTopic() {
        return TopicBuilder.name("user-analysis-topic-aggiorna")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
