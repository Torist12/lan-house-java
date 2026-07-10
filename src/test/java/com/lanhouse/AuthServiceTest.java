package com.lanhouse;

import com.lanhouse.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    @Test
    void deveAutenticarUsuarioAdmPadrao() {
        AuthService authService = new AuthService();

        assertTrue(authService.autenticar("adm", "adm"));
        assertTrue(authService.autenticar("ADM", "ADM"));
    }

    @Test
    void deveRejeitarCredenciaisInvalidas() {
        AuthService authService = new AuthService();

        assertFalse(authService.autenticar("adm", "senhaerrada"));
        assertFalse(authService.autenticar("usuario", "adm"));
    }
}
