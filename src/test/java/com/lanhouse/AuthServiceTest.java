package com.lanhouse;

import com.lanhouse.dao.DatabaseConnection;
import com.lanhouse.dao.FuncionarioDAO;
import com.lanhouse.model.Funcionario;
import com.lanhouse.service.AuthService;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    @Test
    void deveAutenticarUsuarioAdmPadrao() {
        AuthService authService = new AuthService();

        assertTrue(authService.autenticar("adm", "adm"));
        assertTrue(authService.autenticar("ADM", "ADM"));
        assertTrue(authService.isAdminAtual());
    }

    @Test
    void deveRejeitarCredenciaisInvalidas() {
        AuthService authService = new AuthService();

        assertFalse(authService.autenticar("adm", "senhaerrada"));
        assertFalse(authService.autenticar("usuario", "adm"));
    }

    @Test
    void deveEvitarDuplicidadeAoSalvarFuncionarioComMesmoUsuario() throws Exception {
        Files.deleteIfExists(Path.of("lanhouse.db"));
        DatabaseConnection.criarTabelas();

        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        int primeiroId = funcionarioDAO.salvar(new Funcionario("Maria", "hash-1"));
        int segundoId = funcionarioDAO.salvar(new Funcionario("maria", "hash-2"));

        assertTrue(primeiroId > 0);
        assertEquals(primeiroId, segundoId);
        assertEquals("maria", funcionarioDAO.buscarPorUsuario("Maria").getUsuario());
    }
}
