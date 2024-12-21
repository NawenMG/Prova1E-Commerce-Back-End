package com.prova.e_commerce.dbRel.oracle.jdbc.controller.soap;

import com.prova.e_commerce.dbRel.oracle.jdbc.model.User;
import com.prova.e_commerce.dbRel.oracle.jdbc.service.UserService;
import com.example.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.Date;

@Endpoint
public class UserEndpoint {

    private static final String NAMESPACE_URI = "http://example.com/user";

    @Autowired
    private UserService userService;

    // Trova utente per nome utente
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "FindUserByNomeUtenteRequest")
    @ResponsePayload
    public FindUserByNomeUtenteResponse findUserByNomeUtente(@RequestPayload FindUserByNomeUtenteRequest request) {
        FindUserByNomeUtenteResponse response = new FindUserByNomeUtenteResponse();
        Optional<User> userOpt = userService.findByNomeUtente(request.getNomeUtente());

        userOpt.ifPresent(user -> response.setUser(mapToUserType(user)));
        return response;
    }

    // Crea un nuovo utente
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {
        CreateUserResponse response = new CreateUserResponse();
        User user = mapToUserEntity(request.getUser());
        User createdUser = userService.createUser(user);

        response.setUser(mapToUserType(createdUser));
        return response;
    }

    // **Mappa l'entità User su UserType (SOAP JAXB)**
    private UserType mapToUserType(User user) {
        UserType userType = new UserType();
        userType.setId(user.getId());
        userType.setNome(user.getNome());
        userType.setCognome(user.getCognome());
        userType.setRuolo(user.getRuolo());
        userType.setNomeUtente(user.getNomeUtente());
        userType.setEmail(user.getEmail());
        userType.setPassword(user.getPassword());

        // Conversione immagine base64Binary
        if (user.getImmagine() != null) {
            userType.setImmagine(user.getImmagine());
        }

        // Conversione delle date in XMLGregorianCalendar
        if (user.getCreatedAt() != null) {
            userType.setCreatedAt(toXMLGregorianCalendar(user.getCreatedAt()));
        }
        if (user.getUpdatedAt() != null) {
            userType.setUpdatedAt(toXMLGregorianCalendar(user.getUpdatedAt()));
        }
        return userType;
    }

    // **Mappa UserType su entità User (Entity)**
    private User mapToUserEntity(UserType userType) {
        User user = new User();
        user.setId(userType.getId());
        user.setNome(userType.getNome());
        user.setCognome(userType.getCognome());
        user.setRuolo(userType.getRuolo());
        user.setNomeUtente(userType.getNomeUtente());
        user.setEmail(userType.getEmail());
        user.setPassword(userType.getPassword());

        // Conversione immagine base64Binary
        if (userType.getImmagine() != null) {
            user.setImmagine(userType.getImmagine());
        }
        return user;
    }

    // **Conversione Date in XMLGregorianCalendar**
    private XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella conversione della data", e);
        }
    }
}
