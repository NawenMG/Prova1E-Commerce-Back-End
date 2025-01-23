//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.12.23 alle 12:46:09 PM CET 
//


package com.example.user;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nomeUtente" type="{http://example.com/user}String100Type"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nomeUtente"
})
@XmlRootElement(name = "FindUserByNomeUtenteRequest")
public class FindUserByNomeUtenteRequest {

    @XmlElement(required = true)
    protected String nomeUtente;

    /**
     * Recupera il valore della proprietà nomeUtente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeUtente() {
        return nomeUtente;
    }

    /**
     * Imposta il valore della proprietà nomeUtente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeUtente(String value) {
        this.nomeUtente = value;
    }

}
