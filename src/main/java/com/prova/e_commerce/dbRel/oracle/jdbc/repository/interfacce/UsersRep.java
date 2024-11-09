package com.prova.e_commerce.dbRel.oracle.jdbc.repository.interfacce;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;

@Repository
public interface UsersRep {

    List<Users> query(ParamQuery parmQuery, Users users);
    String saveAll(int number);
    String insertUser(Users users);
    String updateUser(int userID, Users users);
    String deleteUser(int userID);
}
