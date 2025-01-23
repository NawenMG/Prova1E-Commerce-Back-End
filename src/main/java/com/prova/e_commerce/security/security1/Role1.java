package com.prova.e_commerce.security.security1;

public enum Role1 {
    USER("User"),
    ADMIN("Admin"),
    CONTROLLER("Controller"),
    USERAI("User AI"),
    USERMONITORING("User Monitoring"),
    USERTRANSITION("User Transition"),
    USERDELIVERY("User Delivery");

    private final String displayName;

    Role1(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return displayName;
    }
}