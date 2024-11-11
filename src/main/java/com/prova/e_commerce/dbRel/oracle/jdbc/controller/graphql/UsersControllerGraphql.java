package com.prova.e_commerce.dbRel.oracle.jdbc.controller.graphql;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.Users;
import com.prova.e_commerce.dbRel.oracle.jdbc.parametri.ParamQuery;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.UsersService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UsersControllerGraphql {
    @Autowired
    private UsersService usersService;

    public List<Users> users(DataFetchingEnvironment env) {
        // Estrazione degli argomenti dal DataFetchingEnvironment
        ParamQuery paramQuery = env.getArgument("paramQuery");
        Users users = env.getArgument("users");
        return usersService.queryUsers(paramQuery, users);
    }
}
