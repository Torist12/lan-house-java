package com.lanhouse.repository.sqlite;

import com.lanhouse.model.Locacao;
import com.lanhouse.model.StatusLocacao;
import com.lanhouse.repository.ILocacaoRepositorio;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDAO implements ILocacaoRepositorio {
    private static final String INSERT = "INSERT INTO locacoes (cliente_id, computador_id, inicio, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, cliente_id, computador_id, inicio, fim, valor_total, status FROM locacoes";
    private static final String SELECT_ATIVAS = SELECT_ALL + " WHERE status = 'ATIVA'";
    private static final String SELECT_BY_CLIENTE = SELECT_ALL + " WHERE cliente_id = ?";
    private static final String UPDATE = "UPDATE locacoes SET fim = ?, valor_total = ?, status = ? WHERE id = ?";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";

    public int iniciarLocacao(Locacao locacao) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            
            pstmt.setInt(1, locacao.getClienteId());
            pstmt.setInt(2, locacao.getComputadorId());
            pstmt.setTimestamp(3, Timestamp.valueOf(locacao.getInicio()));
            pstmt.setString(4, locacao.getStatus().name());
            
            if (pstmt.executeUpdate() > 0) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao iniciar locação no banco de dados: " + e.getMessage(), e);
        }
        return -1;
    }

    public boolean finalizarLocacao(int id, LocalDateTime fim, double valorTotal) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(fim));
            pstmt.setDouble(2, valorTotal);
            pstmt.setString(3, StatusLocacao.FINALIZADA.name());
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao finalizar locação no banco de dados: " + e.getMessage(), e);
        }
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
            throw new RuntimeException("Erro ao buscar locações ativas: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar histórico do cliente: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro no banco de dados SQLite ao carregar o relatório de locações: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar locação por ID: " + e.getMessage(), e);
        }
        return null;
    }

    private Locacao mapearResultSet(ResultSet rs) throws SQLException {
        LocalDateTime fim = rs.getTimestamp("fim") != null
                ? rs.getTimestamp("fim").toLocalDateTime()
                : null;

        return new Locacao(
            rs.getInt("id"),
            rs.getInt("cliente_id"),
            rs.getInt("computador_id"),
            rs.getTimestamp("inicio").toLocalDateTime(),
            fim,
            rs.getDouble("valor_total"),
            StatusLocacao.valueOf(rs.getString("status"))
        );
    }
}
