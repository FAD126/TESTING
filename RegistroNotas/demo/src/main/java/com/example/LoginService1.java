package com.example;

import java.util.Map;

public class LoginService1 {

    // Usuario/clave por defecto (puedes cambiarlos)
    private static final Map<String, String> USERS = new java.util.HashMap<String, String>() {
        {
            put("profesor1", "12345");
        }
    };

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String expected = USERS.get(username.trim());
        return expected != null && expected.equals(password);
    }
}
