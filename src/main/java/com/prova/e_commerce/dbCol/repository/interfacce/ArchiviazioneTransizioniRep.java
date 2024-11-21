package com.prova.e_commerce.dbCol.repository.interfacce;

import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;
import com.prova.e_commerce.dbCol.model.ArchiviazioneTransizioni;

import java.util.List;

public interface ArchiviazioneTransizioniRep {

    List<ArchiviazioneTransizioni> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneTransizioni transizione);

    void saveTransizione(ArchiviazioneTransizioni transizione);

    void updateTransizione(String id, ArchiviazioneTransizioni transizione);

    void deleteTransizione(String id);

    ArchiviazioneTransizioni findTransizioneById(String id);

    List<ArchiviazioneTransizioni> findAllTransizioni();
}
