package com.prova.e_commerce.dbCol.repository.interfacce;

import com.prova.e_commerce.dbCol.model.ArchiviazioneResi;
import com.prova.e_commerce.dbCol.parametri.ParamQueryCassandra;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiviazioneResiRep {

    // Query personalizzate per Cassandra (opzionale)
    ArchiviazioneResi findResoByID(String usersID); // Recupera tutti i resi per un determinato utente
    List<ArchiviazioneResi> queryDinamica(ParamQueryCassandra paramQuery, ArchiviazioneResi resi); // Query dinamica
    void saveReso(ArchiviazioneResi resi, String UserName); // Salva un nuovo reso
    void updateReso(String id, ArchiviazioneResi resi); // Aggiorna un reso esistente, accettazione del reso da parte dell'admin
    void deleteReso(String id); // Elimina un reso
}
