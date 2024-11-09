package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersControllerGraphql implements GraphQLQueryResolver{

    @Autowired
    private UsersService usersService;

    // Query per ottenere ordini
    public List<Users> users(ParamQuery paramQuery, Users users) {
        return usersService.queryUsers(paramQuery, users);
    }
}
