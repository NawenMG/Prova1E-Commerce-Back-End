package com.prova.e_commerce.dbKey.model.SottoClassi;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import javax.validation.constraints.NotNull;

@DynamoDbBean
public class Notifica {

    @NotNull(message = "La notifica via email è obbligatoria")
    private Boolean email;

    @NotNull(message = "La notifica via sms è obbligatoria")
    private Boolean sms;

    @NotNull(message = "Le notifiche sono obbligatorie")
    private Boolean notifiche;

    @NotNull(message = "Le notifiche locali sono obbligatorie")
    private Boolean locali;

    @DynamoDbAttribute("Email")
    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    @DynamoDbAttribute("Sms")
    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    @DynamoDbAttribute("Notifiche")
    public Boolean getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(Boolean notifiche) {
        this.notifiche = notifiche;
    }

    @DynamoDbAttribute("Locali")
    public Boolean getLocali() {
        return locali;
    }

    public void setLocali(Boolean locali) {
        this.locali = locali;
    }
}
