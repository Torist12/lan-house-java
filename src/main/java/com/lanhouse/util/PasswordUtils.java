package com.lanhouse.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class PasswordUtils {
    private static final String ALGORITHM = "SHA-256";

    private PasswordUtils() {
        // Utilitário de apoio, não instanciável.
    }

    public static String hashPassword(String senha) {
        if (senha == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(senha.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash não disponível", e);
        }
    }
}
