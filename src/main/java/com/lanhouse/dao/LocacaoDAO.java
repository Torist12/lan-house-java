package com.lanhouse.dao;

import com.lanhouse.model.Locacao;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO {
    private static final String INSERT = "INSERT INTO locacoes (cliente_id, computador_id, inicio, valor_total, status) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, cliente_id, computador_id, inicio, fim, valor_total, status FROM locacoes";
    private static final String SELECT_ATIVAS = SELECT_ALL + " WHERE status = 'ativa'";
    private static final String SELECT_BY_CLIENTE = SELECT_ALL + " WHERE cliente_id = ?";
    private static final String UPDATE = "UPDATE locacoes SET fim = ?, valor_total = ?, status = ? WHERE id = ?";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";

    public int iniciarLocacao(Locacao locacao) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            
            pstmt.setInt(1, locacao.getClienteId());
            pstmt.setInt(2, locacao.getComputadorId());
            pstmt.setTimestamp(3, Timestamp.valueOf(locacao.getInicio()));
            pstmt.setDouble(4, locacao.getValorTotal());
            pstmt.setString(5, locacao.getStatus());
            
            if (pstmt.executeUpdate() > 0) {
                // Recuperar o último ID inserido
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao iniciar locação: " + e.getMessage());
        }
        return -1;
    }

    public boolean finalizarLocacao(int id, LocalDateTime fim, double valorTotal) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(fim));
            pstmt.setDouble(2, valorTotal);
            pstmt.setString(3, "finalizada");
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao finalizar locação: " + e.getMessage());
        }
        return false;
    }

    public List<Locacao> listarAtivas() {
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ATIVAS)) {
            
            while (rs.next()) {
                locacoes.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar locações ativas: " + e.getMessage());
        }
        return locacoes;
    }

    public List<Locacao> listarPorCliente(int clienteId) {
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_CLIENTE)) {
            
            pstmt.setInt(1, clienteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    locacoes.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar locações do cliente: " + e.getMessage());
        }
        return locacoes;
    }

    public List<Locacao> listarTodas() {
        List<Locacao> locacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                locacoes.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar locações: " + e.getMessage());
        }
        return locacoes;
    }

    public Locacao buscarPorId(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar locação: " + e.getMessage());
        }
        return null;
    }

    private Locacao mapearResultSet(ResultSet rs) throws SQLException {
        LocalDateTime fim = rs.getTimestamp("fim") != null ? rs.getTimestamp("fim").toLocalDateTime() : null;
        
        return new Locacao(
            rs.getInt("id"),
            rs.getInt("cliente_id"),
            rs.getInt("computador_id"),
            rs.getTimestamp("inicio").toLocalDateTime(),
            fim,
            rs.getDouble("valor_total"),
            rs.getString("status")
        );
    }
}
