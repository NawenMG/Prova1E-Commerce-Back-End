//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.12.16 alle 11:35:44 AM CET 
//


package com.example.user;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.user package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.user
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindUserByNomeUtenteRequest }
     * 
     */
    public FindUserByNomeUtenteRequest createFindUserByNomeUtenteRequest() {
        return new FindUserByNomeUtenteRequest();
    }

    /**
     * Create an instance of {@link FindUserByNomeUtenteResponse }
     * 
     */
    public FindUserByNomeUtenteResponse createFindUserByNomeUtenteResponse() {
        return new FindUserByNomeUtenteResponse();
    }

    /**
     * Create an instance of {@link UserType }
     * 
     */
    public UserType createUserType() {
        return new UserType();
    }

    /**
     * Create an instance of {@link CreateUserRequest }
     * 
     */
    public CreateUserRequest createCreateUserRequest() {
        return new CreateUserRequest();
    }

    /**
     * Create an instance of {@link CreateUserResponse }
     * 
     */
    public CreateUserResponse createCreateUserResponse() {
        return new CreateUserResponse();
    }

}
