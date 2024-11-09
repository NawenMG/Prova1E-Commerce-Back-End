package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface UsersRep {

    List<Users> query(ParamQuery parmQuery, Users users);
    void saveAll(int number);
    void insertUser(Users users);
    void updateUser(int userID, Users users);
    void deleteUser(int userID);
}
