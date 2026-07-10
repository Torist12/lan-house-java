package com.lanhouse.dao;

import com.lanhouse.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private static final String INSERT = "INSERT INTO funcionarios (usuario, senha) VALUES (?, ?)";
    private static final String SELECT_ALL = "SELECT id, usuario, senha FROM funcionarios";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_BY_USUARIO = SELECT_ALL + " WHERE LOWER(usuario) = LOWER(?)";
    private static final String UPDATE = "UPDATE funcionarios SET usuario = ?, senha = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM funcionarios WHERE id = ?";

    public int salvar(Funcionario funcionario) {
        if (funcionario == null) {
            return -1;
        }

        String usuario = normalizarUsuario(funcionario.getUsuario());
        if (usuario == null || usuario.isBlank()) {
            return -1;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            Funcionario existente = buscarPorUsuario(conn, usuario);
            if (existente != null) {
                return existente.getId();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, funcionario.getSenha());

                if (pstmt.executeUpdate() > 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                        if (rs.next()) {
                            return rs.getInt(1);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar funcionário: " + e.getMessage());
        }
        return -1;
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                funcionarios.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }
        return funcionarios;
    }

    public Funcionario buscarPorId(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário: " + e.getMessage());
        }
        return null;
    }

    public Funcionario buscarPorUsuario(String usuario) {
        if (usuario == null) {
            return null;
        }

        String usuarioNormalizado = normalizarUsuario(usuario);
        if (usuarioNormalizado == null || usuarioNormalizado.isBlank()) {
            return null;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            return buscarPorUsuario(conn, usuarioNormalizado);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(Funcionario funcionario) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setString(1, funcionario.getUsuario().trim().toLowerCase());
            pstmt.setString(2, funcionario.getSenha());
            pstmt.setInt(3, funcionario.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
        }
        return false;
    }

    public boolean deletar(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar funcionário: " + e.getMessage());
        }
        return false;
    }

    private Funcionario buscarPorUsuario(Connection conn, String usuario) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USUARIO)) {
            pstmt.setString(1, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        }
        return null;
    }

    private String normalizarUsuario(String usuario) {
        if (usuario == null) {
            return null;
        }
        return usuario.trim().toLowerCase();
    }

    private Funcionario mapearResultSet(ResultSet rs) throws SQLException {
        return new Funcionario(
            rs.getInt("id"),
            rs.getString("usuario"),
            rs.getString("senha")
        );
    }
}
