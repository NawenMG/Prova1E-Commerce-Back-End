//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2025.01.23 alle 08:22:31 AM CET 
//


package com.example.user;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per RoleEnum.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="RoleEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="USER"/&gt;
 *     &lt;enumeration value="ADMIN"/&gt;
 *     &lt;enumeration value="CONTROLLER"/&gt;
 *     &lt;enumeration value="USERAI"/&gt;
 *     &lt;enumeration value="USERMONITORING"/&gt;
 *     &lt;enumeration value="USERTRANSITION"/&gt;
 *     &lt;enumeration value="USERDELIVERY"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RoleEnum")
@XmlEnum
public enum RoleEnum {

    USER,
    ADMIN,
    CONTROLLER,
    USERAI,
    USERMONITORING,
    USERTRANSITION,
    USERDELIVERY;

    public String value() {
        return name();
    }

    public static RoleEnum fromValue(String v) {
        return valueOf(v);
    }

}
