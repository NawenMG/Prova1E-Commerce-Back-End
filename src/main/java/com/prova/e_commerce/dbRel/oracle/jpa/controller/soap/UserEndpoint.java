/* package com.prova.e_commerce.dbRel.oracle.jpa.controller.soap;

import com.prova.e_commerce.dbRel.oracle.jpa.entity.Users;
import com.prova.e_commerce.dbRel.oracle.jpa.service.UsersService;
import com.prova.e_commerce.dbRel.oracle.jpa.controller.soap.request.*;
import com.prova.e_commerce.dbRel.oracle.jpa.controller.soap.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Optional;

@Endpoint
public class UserEndpoint {

    private static final String NAMESPACE_URI = "http://prova.com/e_commerce/dbRel/oracle/jpa/users";

    @Autowired
    private UsersService usersService;

    // Operazione SOAP per ottenere un utente per ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByIdRequest")
    @ResponsePayload
    public GetUserByIdResponse getUserById(@RequestPayload GetUserByIdRequest request) {
        Optional<Users> user = usersService.getUserById(request.getId());
        GetUserByIdResponse response = new GetUserByIdResponse();
        response.setUser(user.orElse(null)); // Se l'utente non esiste, restituire null
        return response;
    }

    // Operazione SOAP per ottenere tutti gli utenti
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
    @ResponsePayload
    public GetAllUsersResponse getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        GetAllUsersResponse response = new GetAllUsersResponse();
        response.getUsers().addAll(users);  // Aggiungi tutti gli utenti alla risposta
        return response;
    }

    // Operazione SOAP per ottenere utenti per nome
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersByFirstNameRequest")
    @ResponsePayload
    public GetUsersByFirstNameResponse getUsersByFirstName(@RequestPayload GetUsersByFirstNameRequest request) {
        List<Users> users = usersService.getUsersByFirstName(request.getFirstName());
        GetUsersByFirstNameResponse response = new GetUsersByFirstNameResponse();
        response.getUsers().addAll(users);
        return response;
    }

    // Operazione SOAP per ottenere utenti per cognome
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersByLastNameRequest")
    @ResponsePayload
    public GetUsersByLastNameResponse getUsersByLastName(@RequestPayload GetUsersByLastNameRequest request) {
        List<Users> users = usersService.getUsersByLastName(request.getLastName());
        GetUsersByLastNameResponse response = new GetUsersByLastNameResponse();
        response.getUsers().addAll(users);
        return response;
    }

    // Operazione SOAP per ottenere un utente per username
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByUsernameRequest")
    @ResponsePayload
    public GetUserByUsernameResponse getUserByUsername(@RequestPayload GetUserByUsernameRequest request) {
        Optional<Users> user = usersService.getUserByUsername(request.getUsername());
        GetUserByUsernameResponse response = new GetUserByUsernameResponse();
        response.setUser(user.orElse(null));
        return response;
    }

    // Operazione SOAP per ottenere un utente per email
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByEmailRequest")
    @ResponsePayload
    public GetUserByEmailResponse getUserByEmail(@RequestPayload GetUserByEmailRequest request) {
        Optional<Users> user = usersService.getUserByEmail(request.getEmail());
        GetUserByEmailResponse response = new GetUserByEmailResponse();
        response.setUser(user.orElse(null));
        return response;
    }

    // Operazione SOAP per aggiungere un utente
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload AddUserRequest request) {
        usersService.addUser(request.getUser());
        AddUserResponse response = new AddUserResponse();
        response.setStatus("User added successfully.");
        return response;
    }

    // Operazione SOAP per aggiornare un utente
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        usersService.updateUser(request.getUser());
        UpdateUserResponse response = new UpdateUserResponse();
        response.setStatus("User updated successfully.");
        return response;
    }

    // Operazione SOAP per eliminare un utente per ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserByIdRequest")
    @ResponsePayload
    public DeleteUserByIdResponse deleteUserById(@RequestPayload DeleteUserByIdRequest request) {
        usersService.deleteUserById(request.getId());
        DeleteUserByIdResponse response = new DeleteUserByIdResponse();
        response.setStatus("User deleted successfully.");
        return response;
    }
}
 */