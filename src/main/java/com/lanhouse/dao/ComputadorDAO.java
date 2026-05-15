package com.lanhouse.dao;

import com.lanhouse.model.Computador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputadorDAO {
    private static final String SELECT_ALL = "SELECT id, numero, status, tier, preco_hora FROM computadores";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_BY_NUMERO = SELECT_ALL + " WHERE numero = ?";
    private static final String SELECT_LIVRES = SELECT_ALL + " WHERE status = 'livre'";
    private static final String UPDATE_STATUS = "UPDATE computadores SET status = ? WHERE id = ?";

    public List<Computador> listarTodos() {
        List<Computador> computadores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                computadores.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar computadores: " + e.getMessage());
        }
        return computadores;
    }

    public Computador buscarPorId(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar computador: " + e.getMessage());
        }
        return null;
    }

    public Computador buscarPorNumero(int numero) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_NUMERO)) {

            pstmt.setInt(1, numero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar computador: " + e.getMessage());
        }
        return null;
    }

    public List<Computador> listarLivres() {
        List<Computador> computadores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_LIVRES)) {

            while (rs.next()) {
                computadores.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar computadores livres: " + e.getMessage());
        }
        return computadores;
    }

    public boolean atualizarStatus(int id, String novoStatus) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_STATUS)) {

            pstmt.setString(1, novoStatus);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do computador: " + e.getMessage());
        }
        return false;
    }

    private Computador mapearResultSet(ResultSet rs) throws SQLException {
        return new Computador(
            rs.getInt("id"),
            rs.getInt("numero"),
            rs.getString("status"),
            rs.getString("tier"),
            rs.getDouble("preco_hora")
        );
    }
}