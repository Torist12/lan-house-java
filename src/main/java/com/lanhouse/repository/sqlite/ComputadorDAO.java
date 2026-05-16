package com.lanhouse.repository.sqlite;

// If a separate DatabaseConnection class is not available on the classpath,
// provide a simple package-private fallback for SQLite connections.
// This keeps changes local to this file as requested.
import com.lanhouse.model.Computador;
import com.lanhouse.model.StatusComputador;
import com.lanhouse.model.TierComputador;
import com.lanhouse.repository.IComputadorRepositorio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputadorDAO implements IComputadorRepositorio {
    private static final String INSERT = "INSERT INTO computadores (numero, status, tier, preco_hora) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, numero, status, tier, preco_hora FROM computadores";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_BY_NUMERO = SELECT_ALL + " WHERE numero = ?";
    private static final String SELECT_LIVRES = SELECT_ALL + " WHERE status = 'LIVRE'";
    private static final String UPDATE = "UPDATE computadores SET numero = ?, status = ?, tier = ?, preco_hora = ? WHERE id = ?";
    private static final String UPDATE_STATUS = "UPDATE computadores SET status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM computadores WHERE id = ?";

    public int salvar(Computador computador) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            
            pstmt.setInt(1, computador.getNumero());
            pstmt.setString(2, computador.getStatus().name());
            pstmt.setString(3, computador.getTier().name());
            pstmt.setDouble(4, computador.getPrecoHora());
            
            if (pstmt.executeUpdate() > 0) {
                return getLastInsertId(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar computador no banco: " + e.getMessage(), e);
        }
        return -1;
    }

    public List<Computador> listarTodos() {
        List<Computador> computadores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                computadores.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar computadores no banco: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar computador por ID: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar computador por número: " + e.getMessage(), e);
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
            throw new RuntimeException("Erro ao buscar computadores disponíveis: " + e.getMessage(), e);
        }
        return computadores;
    }

    public boolean atualizar(Computador computador) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {

            pstmt.setInt(1, computador.getNumero());
            pstmt.setString(2, computador.getStatus().name());
            pstmt.setString(3, computador.getTier().name());
            pstmt.setDouble(4, computador.getPrecoHora());
            pstmt.setInt(5, computador.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar computador: " + e.getMessage(), e);
        }
    }

    public boolean atualizarStatus(int id, StatusComputador novoStatus) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_STATUS)) {

            pstmt.setString(1, novoStatus.name());
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do computador no banco: " + e.getMessage(), e);
        }
    }

    public boolean deletar(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar computador do banco: " + e.getMessage(), e);
        }
    }

    private int getLastInsertId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private Computador mapearResultSet(ResultSet rs) throws SQLException {
        return new Computador(
            rs.getInt("id"),
            rs.getInt("numero"),
            StatusComputador.valueOf(rs.getString("status")),
            TierComputador.valueOf(rs.getString("tier")),
            rs.getDouble("preco_hora")
        );
    }
}

// Fallback DatabaseConnection implementation (package-private).
// Adjust the JDBC URL as needed for your environment.
// DatabaseConnection is provided in DatabaseConnection.java in this package.
