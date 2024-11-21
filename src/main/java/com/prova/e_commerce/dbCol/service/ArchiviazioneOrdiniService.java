package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.model.ArchiviazioneOrdini;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneOrdiniRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneOrdiniService {

    @Autowired
    private ArchiviazioneOrdiniRep archiviazioneOrdiniRep;

    // Metodo per eseguire una query dinamica sugli ordini
    public List<ArchiviazioneOrdini> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneOrdini ordine) {
        return archiviazioneOrdiniRep.queryDinamica(paramQuery, ordine);
    }

    // Metodo per salvare un nuovo ordine
    public void saveOrdine(ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniRep.saveOrdine(ordine);
    }

    // Metodo per aggiornare un ordine esistente
    public void updateOrdine(String id, ArchiviazioneOrdini ordine) {
        archiviazioneOrdiniRep.updateOrdine(id, ordine);
    }

    // Metodo per eliminare un ordine
    public void deleteOrdine(String id) {
        archiviazioneOrdiniRep.deleteOrdine(id);
    }

    // Metodo per trovare un ordine tramite ID
    public ArchiviazioneOrdini findOrdineById(String id) {
        return archiviazioneOrdiniRep.findOrdineById(id);
    }

    // Metodo per trovare tutti gli ordini
    public List<ArchiviazioneOrdini> findAllOrdini() {
        return archiviazioneOrdiniRep.findAllOrdini();
    }
}
