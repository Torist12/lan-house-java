package com.lanhouse.service;

import com.lanhouse.dao.FuncionarioDAO;
import com.lanhouse.model.Funcionario;
import com.lanhouse.util.PasswordUtils;

import java.util.Locale;

public class AuthService {
    private static final String DEFAULT_USER = "adm";
    private static final String DEFAULT_PASSWORD = "adm";

    private final FuncionarioDAO funcionarioDAO;
    private boolean adminAtual;

    public AuthService() {
        this(new FuncionarioDAO());
    }

    public AuthService(FuncionarioDAO funcionarioDAO) {
        this.funcionarioDAO = funcionarioDAO;
    }

    public boolean autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) {
            return false;
        }

        String usuarioNormalizado = usuario.trim().toLowerCase(Locale.ROOT);
        String senhaTexto = senha.trim();

        adminAtual = false;

        Funcionario funcionario = funcionarioDAO.buscarPorUsuario(usuarioNormalizado);
        if (funcionario != null) {
            String senhaArmazenada = funcionario.getSenha();
            if (senhaArmazenada != null && senhaArmazenada.equals(senhaTexto)) {
                adminAtual = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado);
                return true;
            }
            if (senhaArmazenada != null && senhaArmazenada.equals(PasswordUtils.hashPassword(senhaTexto))) {
                adminAtual = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado);
                return true;
            }
            if (senhaArmazenada != null && senhaArmazenada.equals(PasswordUtils.hashPassword(senhaTexto.toLowerCase(Locale.ROOT)))) {
                adminAtual = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado);
                return true;
            }
            if (senhaArmazenada != null && senhaArmazenada.equals(PasswordUtils.hashPassword(senhaTexto.toUpperCase(Locale.ROOT)))) {
                adminAtual = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado);
                return true;
            }
            if (senhaArmazenada != null && (senhaArmazenada.equals(senhaTexto.toLowerCase(Locale.ROOT))
                    || senhaArmazenada.equals(senhaTexto.toUpperCase(Locale.ROOT)))) {
                funcionario.setSenha(senhaTexto);
                funcionarioDAO.atualizar(funcionario);
                adminAtual = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado);
                return true;
            }
            return false;
        }

        boolean sucesso = DEFAULT_USER.equalsIgnoreCase(usuarioNormalizado)
                && DEFAULT_PASSWORD.equalsIgnoreCase(senhaTexto);
        adminAtual = sucesso;
        return sucesso;
    }

    public boolean isAdminAtual() {
        return adminAtual;
    }
}
