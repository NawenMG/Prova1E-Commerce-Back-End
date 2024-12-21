//
// Questo file è stato generato dall'Eclipse Implementation of JAXB, v3.0.0 
// Vedere https://eclipse-ee4j.github.io/jaxb-ri 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2024.12.16 alle 10:26:16 AM CET 
//


package generated;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per UserType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="UserType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{}UUIDType"/&gt;
 *         &lt;element name="nome" type="{}String100Type"/&gt;
 *         &lt;element name="cognome" type="{}String100Type"/&gt;
 *         &lt;element name="ruolo" type="{}String50Type"/&gt;
 *         &lt;element name="nomeUtente" type="{}String100Type"/&gt;
 *         &lt;element name="email" type="{}EmailType"/&gt;
 *         &lt;element name="password" type="{}PasswordType"/&gt;
 *         &lt;element name="immagine" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="updatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserType", propOrder = {
    "id",
    "nome",
    "cognome",
    "ruolo",
    "nomeUtente",
    "email",
    "password",
    "immagine",
    "createdAt",
    "updatedAt"
})
public class UserType {

    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String nome;
    @XmlElement(required = true)
    protected String cognome;
    @XmlElement(required = true)
    protected String ruolo;
    @XmlElement(required = true)
    protected String nomeUtente;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    protected String password;
    protected byte[] immagine;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdAt;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedAt;

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietà cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietà ruolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il valore della proprietà ruolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuolo(String value) {
        this.ruolo = value;
    }

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

    /**
     * Recupera il valore della proprietà email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta il valore della proprietà email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Recupera il valore della proprietà password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta il valore della proprietà password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Recupera il valore della proprietà immagine.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getImmagine() {
        return immagine;
    }

    /**
     * Imposta il valore della proprietà immagine.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setImmagine(byte[] value) {
        this.immagine = value;
    }

    /**
     * Recupera il valore della proprietà createdAt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Imposta il valore della proprietà createdAt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Recupera il valore della proprietà updatedAt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Imposta il valore della proprietà updatedAt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdatedAt(XMLGregorianCalendar value) {
        this.updatedAt = value;
    }

}
