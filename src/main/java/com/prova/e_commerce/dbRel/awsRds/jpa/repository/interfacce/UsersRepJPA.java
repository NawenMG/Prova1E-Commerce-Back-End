package com.prova.e_commerce.dbRel.awsRds.jpa.repository.interfacce;

import com.prova.e_commerce.dbRel.awsRds.jpa.entity.Users;
//import com.prova.e_commerce.dbRel.oracle.jpa.parametri.ParamQueryJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.util.List;

@Repository
public interface UsersRepJPA extends JpaRepository<Users, String> {

   /*  // Query dinamiche (se necessario)
    List<Users> findByParamQueryAndUsers(ParamQueryJPA paramQuery, Users users);

    // Salvataggio di più utenti
    //void saveAll(int number);

    // Eliminarlo per ID
    void deleteById(String userId); */
}
