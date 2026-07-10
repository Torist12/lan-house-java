package com.lanhouse.service;

public class AuthService {
    private static final String DEFAULT_USER = "adm";
    private static final String DEFAULT_PASSWORD = "adm";

    public boolean autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) {
            return false;
        }

        String usuarioNormalizado = usuario.trim();
        String senhaNormalizada = senha.trim();

        return DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado)
                && DEFAULT_PASSWORD.equalsIgnoreCase(senhaNormalizada);
    }
}
