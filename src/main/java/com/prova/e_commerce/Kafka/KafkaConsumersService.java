package com.prova.e_commerce.kafka;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.model.ArchiviazioneSegnalazioni;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import com.prova.e_commerce.dbDoc.entity.Recensioni;
import com.prova.e_commerce.dbDoc.entity.SchedeProdotti;
import com.prova.e_commerce.dbG.model.NodoCategoriaProdotto;
import com.prova.e_commerce.dbG.model.NodoLocazioneUtente;
import com.prova.e_commerce.dbG.model.NodoProdotto;
import com.prova.e_commerce.dbKey.model.Carrello;
import com.prova.e_commerce.dbKey.model.Cronologia;
import com.prova.e_commerce.dbKey.model.WishList;
import com.prova.e_commerce.dbRT.model.ShippingStatus;
import com.prova.e_commerce.dbRel.awsRds.jpa.entity.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Categorie;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Ordini;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Pagamenti;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Prodotti;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Resi;
import com.prova.e_commerce.dbTS.model.SalesMonitoring;
import com.prova.e_commerce.dbTS.model.ServerResponse;
import com.prova.e_commerce.dbTS.model.TrafficAnalysis;
import com.prova.e_commerce.dbTS.model.UserAnalysis;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumersService {  // RICORDATI CHE OGNI GRUPPO PUO' ESSERE PERSONALIZZATO NELLE PROPERTIES

    // ARCHIVIAZIONE ORDINI

    @KafkaListener(topics = "ordini-topic-save", groupId = "ordini")
    public void consumeOrdineCreato( ArchiviazioneOrdini ordine) {
            System.out.println("Ricevuto evento da ordini-topic-save: OrdineCreato");
            System.out.println("Dettagli ordine ricevuto: " + ordine);
    }

    @KafkaListener(topics = "ordini-topic-update", groupId = "ordini")
    public void consumeOrdineAggiornato( ArchiviazioneOrdini ordine) {
            System.out.println("Ricevuto evento da ordini-topic-update: OrdineAggiornato");
            System.out.println("Dettagli ordine ricevuto: " + ordine);
    }

    // ARCHIVIAZIONE SEGNALAZIONI

    @KafkaListener(topics = "segnalazioni-topic-save", groupId = "segnalazioni")
    public void consumeSegnalazioneCreato( ArchiviazioneSegnalazioni segnalazione) {
            System.out.println("Ricevuto evento da segnalazioni-topic-save: SegnalazioneCreata");
            System.out.println("Dettagli segnalazione ricevuto: " + segnalazione);
    }

    @KafkaListener(topics = "segnalazioni-topic-update", groupId = "segnalazioni")
    public void consumeSegnalazioneAggiornata( ArchiviazioneSegnalazioni segnalazione) {
            System.out.println("Ricevuto evento da segnalazioni-topic-update: SegnalazioneAggiornata");
            System.out.println("Dettagli segnalazione ricevuto: " + segnalazione);
    }

    // ARCHIVIAZIONE TRANSIZIONI

    @KafkaListener(topics = "transizioni-topic-save", groupId = "transizioni")
    public void consumeTransizioneCreato( ArchiviazioneTransizioni transizione) {
            System.out.println("Ricevuto evento da transizioni-topic-save: TransizioneCreata");
            System.out.println("Dettagli transizione ricevuto: " + transizione);
    }

    @KafkaListener(topics = "transizioni-topic-update", groupId = "transizioni")
    public void consumeTransizioneAggiornata( ArchiviazioneTransizioni transizione) {
            System.out.println("Ricevuto evento da transizioni-topic-update: TransizioneAggiornata");
            System.out.println("Dettagli transizione ricevuto: " + transizione);
    }

    // RECENSIONI

    @KafkaListener(topics = "recensioni-topic-save", groupId = "recensioni")
    public void consumeRecensioneCreato( Recensioni recensione) {
            System.out.println("Ricevuto evento da recensioni-topic-save: RecensioneCreata");
            System.out.println("Dettagli recensione ricevuta: " + recensione);
    }

    @KafkaListener(topics = "recensioni-topic-update", groupId = "recensioni")
    public void consumeRecensioneAggiornata(String key, Recensioni recensione) {
            System.out.println("Ricevuto evento da recensioni-topic-update: RecensioneAggiornata");
            System.out.println("Dettagli recensione aggiornata: " + recensione);
    }

    // SCHEDA PRODOTTI

    @KafkaListener(topics = "scheda-prodotti-topic-save", groupId = "scheda-prodotti")
    public void consumeProdottoCreato( SchedeProdotti prodotto) {
            System.out.println("Ricevuto evento da scheda-prodotti-topic-save: ProdottoCreato");
            System.out.println("Dettagli prodotto ricevuto: " + prodotto);
    }

    @KafkaListener(topics = "scheda-prodotti-topic-update", groupId = "scheda-prodotti")
    public void consumeProdottoAggiornato( SchedeProdotti prodotto) {
            System.out.println("Ricevuto evento da scheda-prodotti-topic-update: ProdottoAggiornato");
            System.out.println("Dettagli prodotto aggiornato: " + prodotto);
    }

    // EVENTI DI SERVICEGRAPHDB

    @KafkaListener(topics = "eventi-prodotti-topic-visita", groupId = "eventi-prodotti-visita")
    public void consumeVisitaProdotto( NodoProdotto nodoProdotto) {
            System.out.println("Ricevuto evento da eventi-prodotti-topic-visita: ProdottoVisitato");
            System.out.println("Dettagli evento visita prodotto: " + nodoProdotto);
    }

    @KafkaListener(topics = "eventi-prodotti-topic-acquista", groupId = "eventi-prodotti-acquisto")
    public void consumeAcquistoProdotto( NodoProdotto nodoProdotto) {
            System.out.println("Ricevuto evento da eventi-prodotti-topic-acquista: ProdottoAcquistato");
            System.out.println("Dettagli evento acquisto prodotto: " + nodoProdotto);
    }

    @KafkaListener(topics = "eventi-appartenenza-categoria", groupId = "eventi-appartenenza-categoria")
    public void consumeAppartenenzaCategoria( NodoCategoriaProdotto nodoCategoriaProdotto) {
            System.out.println("Ricevuto evento da eventi-appartenenza-categoria: ProdottoCategoriaAggiunta");
            System.out.println("Dettagli evento appartenenza alla categoria: " + nodoCategoriaProdotto);
    }

    @KafkaListener(topics = "eventi-provenienza-utenti", groupId = "eventi-provenienza-utenti")
    public void consumeProvenienzaGeografica( NodoLocazioneUtente nodoLocazioneUtente) {
            System.out.println("Ricevuto evento da eventi-provenienza-utenti: ProdottoProvenienzaGeografica");
            System.out.println("Dettagli evento provenienza geografica: " + nodoLocazioneUtente);
    }

    // EVENTI CRONOLOGIA

    @KafkaListener(topics = "eventi-cronologia-topic-aggiungi", groupId = "cronologia-group")
    public void consumeCronologiaAggiunta( Cronologia cronologia) {
            System.out.println("Ricevuto evento da eventi-cronologia-topic-aggiungi: ProdottiAggiuntiCronologia");
            System.out.println("Dettagli evento: " + cronologia);
    }

    // EVENTI CARRELLO

    @KafkaListener(topics = "eventi-carrello-topic-aggiungi", groupId = "carrello")
    public void consumeCarrelloAggiunta( Carrello carrello) {
            System.out.println("Ricevuto evento da eventi-carrello-topic-aggiungi: ProdottiAggiunti");
            System.out.println("Dettagli evento aggiunta prodotti al carrello: " + carrello);
    }

     // EVENTI WISHLIST

     @KafkaListener(topics = "eventi-wishlist-topic-aggiungi", groupId = "wishlist-group")
     public void consumeWishlistAggiunta( WishList wishList) {
             System.out.println("Ricevuto evento da eventi-wishlist-topic-aggiungi: ProdottiAggiuntiWishlist");
             System.out.println("Dettagli evento: " + wishList);
     }

     //Users

     @KafkaListener(topics = "eventi-utente-topic-aggiungi", groupId = "users-group")
     public void consumeUsersAggiungi( Users users){
            System.out.println("Ricevuto evento da eventi-utente-topic-aggiungi: UserAggiunto");
            System.out.println("Dettagli evento:" + users);
     }

     @KafkaListener(topics = "eventi-utente-topic-aggiorna", groupId = "users-group")
     public void consumeUsersAggiorna( Users users){
            System.out.println("Ricevuto evento da eventi-utente-topic-aggiorna: UserAggiornato");
            System.out.println("Dettagli evento:" + users);
     }

     //Categorie

    @KafkaListener(topics = "eventi-categoria-topic-aggiungi", groupId = "categorie-group")
    public void consumeCategoriaAggiunta( Categorie categorie) {
            System.out.println("Ricevuto evento da eventi-categoria-topic-aggiungi: CategoriaAggiunta");
            System.out.println("Dettagli evento categoria aggiunta: " + categorie);
    }

    @KafkaListener(topics = "eventi-categoria-topic-aggiorna", groupId = "categorie-group")
    public void consumeCategoriaAggiornata( Categorie categorie) {
            System.out.println("Ricevuto evento da eventi-categoria-topic-aggiorna: CategoriaAggiornata");
            System.out.println("Dettagli evento categoria aggiornata: " + categorie);
    }

    //Ordini

     @KafkaListener(topics = "ordini-topic-aggiungi", groupId = "pagamenti-group")
     public void consumeOrdineAggiunto( Ordini ordini) {
             System.out.println("Ricevuto evento da ordini-topic-aggiungi: OrdineAggiunto");
             System.out.println("Dettagli evento ordine aggiunto: " + ordini);
     }
 
     @KafkaListener(topics = "ordini-topic-aggiorna", groupId = "pagamenti-group")
     public void consumeOrdineAggiornato( Ordini ordini) {
             System.out.println("Ricevuto evento da ordini-topic-aggiorna: OrdineAggiornato");
             System.out.println("Dettagli evento ordine aggiornato: " + ordini);
     }

     //Pagamenti

    // Ascolta i messaggi nel topic "pagamenti-topic-aggiungi"
    @KafkaListener(topics = "pagamenti-topic-aggiungi", groupId = "pagamenti-group")
    public void consumePagamentoAggiunto( Pagamenti pagamenti) {
            System.out.println("Ricevuto evento da pagamenti-topic-aggiungi: PagamentoAggiunto");
            System.out.println("Dettagli evento pagamento aggiunto: " + pagamenti);
    }

    // Ascolta i messaggi nel topic "pagamenti-topic-aggiorna"
    @KafkaListener(topics = "pagamenti-topic-aggiorna", groupId = "pagamenti-group")
    public void consumePagamentoAggiornato( Pagamenti pagamenti) {
            System.out.println("Ricevuto evento da pagamenti-topic-aggiorna: PagamentoAggiornato");
            System.out.println("Dettagli evento pagamento aggiornato: " + pagamenti);
    }

    //Prodotti

     @KafkaListener(topics = "prodotti-topic-aggiungi", groupId = "prodotti-group")
     public void consumeProdottoAggiunto( Prodotti prodotti) {
             System.out.println("Ricevuto evento da prodotti-topic-aggiungi: ProdottoAggiunto");
             System.out.println("Dettagli evento prodotto aggiunto: " + prodotti);
     }
 
     @KafkaListener(topics = "prodotti-topic-aggiorna", groupId = "prodotti-group")
     public void consumeProdottoAggiornato( Prodotti prodotti) {
             System.out.println("Ricevuto evento da prodotti-topic-aggiorna: ProdottoAggiornato");
             System.out.println("Dettagli evento prodotto aggiornato: " + prodotti);
     }

     //Resi

    @KafkaListener(topics = "resi-topic-aggiungi", groupId = "resi-group")
    public void consumeResoAggiunto( Resi resi) {
            System.out.println("Ricevuto evento da resi-topic-aggiungi: ResoAggiunto");
            System.out.println("Dettagli evento reso aggiunto: " + resi);
    }

    @KafkaListener(topics = "resi-topic-aggiorna", groupId = "resi-group")
    public void consumeResoAggiornato( Resi resi) {
            System.out.println("Ricevuto evento da resi-topic-aggiorna: ResoAggiornato");
            System.out.println("Dettagli evento reso aggiornato: " + resi);
    }

    //Shipping-Status

    @KafkaListener(topics = "shipping-status-topic-aggiungi", groupId = "shipping-status-group")
    public void consumeShippingStatusCreated(ShippingStatus shippingStatus) {
        System.out.println("Ricevuto messaggio dal topic 'shipping-status-topic-aggiungi':");
        System.out.println("Nuovo stato di spedizione creato: " + shippingStatus);
    }

    @KafkaListener(topics = "shipping-status-topic-aggiorna", groupId = "shipping-status-group")
    public void consumeShippingStatusUpdated(ShippingStatus shippingStatus) {
        System.out.println("Ricevuto messaggio dal topic 'shipping-status-topic-aggiorna':");
        System.out.println("Stato di spedizione aggiornato: " + shippingStatus);
    }

    //Sales-monitoring
     @KafkaListener(topics = "sales-monitoring-topic-aggiungi", groupId = "sales-monitoring-group")
     public void consumeSalesMonitoringCreated(SalesMonitoring salesMonitoring) {
         System.out.println("Ricevuto messaggio dal topic 'sales-monitoring-topic-aggiungi':");
         System.out.println("Controllo delle vendite aggiunto:" + salesMonitoring);  
     }
 
     @KafkaListener(topics = "sales-monitoring-topic-aggiorna", groupId = "sales-monitoring-group")
     public void consumeSalesMonitoringUpdated(SalesMonitoring salesMonitoring) {
         System.out.println("Ricevuto messaggio dal topic 'sales-monitoring-topic-aggiorna':");
         System.out.println("Controllo delle vendite aggiornato:" + salesMonitoring);  
     }

     //Server_response
    @KafkaListener(topics = "server-response-topic-aggiungi", groupId = "server-response-group")
    public void consumeServerResponseCreated(ServerResponse serverResponse) {
        System.out.println("Ricevuto messaggio dal topic 'server-response-topic-aggiungi':");
        System.out.println("Server response:" + serverResponse);  
    }

    // Ascolta i messaggi nel topic "server-response-topic-aggiorna"
    @KafkaListener(topics = "server-response-topic-aggiorna", groupId = "server-response-group")
    public void consumeServerResponseUpdated(ServerResponse serverResponse) {
        System.out.println("Ricevuto messaggio dal topic 'server-response-topic-aggiorna':");
        System.out.println("Server Response" + serverResponse);  
    }

    //Traffic-analysis
    @KafkaListener(topics = "traffic-analysis-topic-aggiungi", groupId = "traffic-analysis-group")
    public void consumeTrafficAnalysisCreated(TrafficAnalysis trafficAnalysis) {
        System.out.println("Ricevuto messaggio dal topic 'traffic-analysis-topic-aggiungi':");
        System.out.println("TrafficAnalysis:" + trafficAnalysis);
    }

    //User-analysis
    @KafkaListener(topics = "user-analysis-topic-aggiungi", groupId = "user-analysis-group")
    public void consumeUserAnalysisCreated(UserAnalysis userAnalysis) {
        System.out.println("Ricevuto messaggio dal topic 'user-analysis-topic-aggiungi':");
        System.out.println("User-analysis:" + userAnalysis);  
    }

    @KafkaListener(topics = "user-analysis-topic-aggiorna", groupId = "user-analysis-group")
    public void consumeUserAnalysisUpdated(UserAnalysis userAnalysis) {
        System.out.println("Ricevuto messaggio dal topic 'user-analysis-topic-aggiorna':");
        System.out.println("User-analysis:" + userAnalysis);  
    }

}
