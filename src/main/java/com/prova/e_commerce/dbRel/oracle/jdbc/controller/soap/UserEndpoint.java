/* package com.prova.e_commerce.dbRel.oracle.jdbc.controller.soap;

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
 */


 package com.prova.e_commerce.dbRel.oracle.jdbc.controller.soap;

import com.example.user.CreateUserRequest;
import com.example.user.CreateUserResponse;
import com.example.user.GetUserRequest;
import com.example.user.GetUserResponse;
import com.example.user.UserType;

import com.prova.e_commerce.security.security1.User1;
import com.prova.e_commerce.security.security1.User1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * Endpoint SOAP per la gestione degli utenti.
 * Espone i metodi definiti nello schema XSD (User.xsd).
 */
@Endpoint
@Component
public class UserEndpoint {

    // Namespace definito nel tuo User.xsd (targetNamespace)
    private static final String NAMESPACE_URI = "http://example.com/user";

    @Autowired
    private User1Service user1Service;

    /**
     * Operazione: GetUser
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        GetUserResponse response = new GetUserResponse();

        // Tenta di trovare l'utente per username
        try {
            User1 userEntity = user1Service.findByUsername(request.getUsername());
            UserType userType = toUserType(userEntity);
            response.setUser(userType);
        } catch (Exception e) {
            // Se non trovato, setta user a null
            response.setUser(null);
        }
        return response;
    }

    /**
     * Operazione: CreateUser
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {
        CreateUserResponse response = new CreateUserResponse();

        // Verifica se username esiste già
        if (user1Service.existsByUsername(request.getUser().getUsername())) {
            // Se esiste, restituisce errore
            response.setSuccess(false);
            response.setMessage("Username già in uso!");
            return response;
        }

        // Crea un nuovo user1 a partire da userType
        User1 newUserEntity = toEntity(request.getUser());
        user1Service.save(newUserEntity);

        // Prepara la risposta
        response.setUser(toUserType(newUserEntity));
        response.setSuccess(true);
        response.setMessage("Utente creato con successo!");
        return response;
    }

    // ============================================================
    //         Metodi di conversione tra User1 e UserType
    // ============================================================

    /**
     * Converte l'entity User1 in un oggetto UserType (JAXB).
     */
    private UserType toUserType(User1 userEntity) {
        if (userEntity == null) return null;

        UserType userType = new UserType();
        userType.setId(userEntity.getId());
        userType.setNome(userEntity.getNome());
        userType.setCognome(userEntity.getCognome());
        userType.setUsername(userEntity.getUsername());
        userType.setPassword(userEntity.getPassword());
        userType.setEmail(userEntity.getEmail());
        userType.setImmagine(userEntity.getImmagine());

        // Se l'XSD prevede dateTime in formato XMLGregorianCalendar,
        // puoi (opzionalmente) convertire da java.util.Date a XMLGregorianCalendar
        // userType.setCreatedAt(toXMLGregorianCalendar(userEntity.getCreatedAt()));
        // userType.setUpdatedAt(toXMLGregorianCalendar(userEntity.getUpdatedAt()));

        userType.setTwoFactorEnabled(userEntity.isTwoFactorEnabled());
        userType.setBlocked(userEntity.isBlocked());

        // Se hai ruoli (Role1) e un enumerato corrispondente nell'XSD:
        // per ogni role, aggiungi userType.getRoles().add(role.name());

        return userType;
    }

    /**
     * Converte un oggetto UserType (JAXB) in un'entity User1.
     */
    private User1 toEntity(UserType userType) {
        User1 userEntity = new User1();
        userEntity.setNome(userType.getNome());
        userEntity.setCognome(userType.getCognome());
        userEntity.setUsername(userType.getUsername());
        userEntity.setPassword(userType.getPassword());
        userEntity.setEmail(userType.getEmail());
        userEntity.setImmagine(userType.getImmagine());
        // etc...
        userEntity.setTwoFactorEnabled(userType.isTwoFactorEnabled());
        userEntity.setBlocked(userType.isBlocked());

        // Se devi gestire roles e userType.getRoles() (lista di enumerati XSD),
        // userEntity.setRoles(...); // convertili in Set<Role1>

        return userEntity;
    }

    /* 
     * (Opzionale)
     * Esempio di conversione da Date a XMLGregorianCalendar, 
     * se l'XSD e la classe generata usano XMLGregorianCalendar per dateTime.
     */
    /*
    private XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) return null;
        try {
            DatatypeFactory df = DatatypeFactory.newInstance();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            return df.newXMLGregorianCalendar(gc);
        } catch (Exception e) {
            return null;
        }
    }
    */
}
