package com.prova.e_commerce.dbCol.service;

import com.prova.e_commerce.dbCol.repository.interfacce.ArchiviazioneTransizioniRep;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiviazioneTransizioniService
{
    @Autowired
    private ArchiviazioneTransizioniRep archiviazioneTransizioniRep;

    public List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneTransizioni transizione) {
        return archiviazioneTransizioniRep.queryDinamica(paramQuery, transizione);
    }

    public void saveTransizione(ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniRep.saveTransizione(transizione);
    }

    public void updateTransizione(String id, ArchiviazioneTransizioni transizione) {
        archiviazioneTransizioniRep.updateTransizione(id, transizione);
    }

    public void deleteTransizione(String id) {
        archiviazioneTransizioniRep.deleteTransizione(id);
    }

    public ArchiviazioneTransizioni findTransizioneById(String id) {
        return archiviazioneTransizioniRep.findTransizioneById(id);
    }

    public List<ArchiviazioneTransizioni> findAllTransizioni() {
        return archiviazioneTransizioniRep.findAllTransizioni();
    }
}
