package com.prova.e_commerce.dbCol.repository.interfacce;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import java.util.List;

public interface ArchiviazioneOrdiniRep {

    // Metodo per eseguire una query dinamica sugli ordini
    List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine);

    // Metodo per salvare un nuovo ordine
    void saveOrdine(ArchiviazioneOrdini ordine);

    // Metodo per aggiornare un ordine esistente
    void updateOrdine(String id, ArchiviazioneOrdini ordine);

    // Metodo per eliminare un ordine
    void deleteOrdine(String id);

    // Metodo per trovare un ordine tramite ID
    ArchiviazioneOrdini findOrdineById(String id);

    // Metodo per trovare tutti gli ordini
    List<ArchiviazioneOrdini> findAllOrdini();
}
