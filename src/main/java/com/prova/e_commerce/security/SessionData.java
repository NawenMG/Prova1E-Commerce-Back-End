package com.prova.e_commerce.security;

import lombok.Data;

@Data
public class SessionData {
    private String username;
    private String role;
    private long lastAccessTime;
}
