package com.lanhouse.repository.sqlite;

import com.lanhouse.model.Computador;
import com.lanhouse.model.StatusComputador;
import com.lanhouse.model.TierComputador;
import com.lanhouse.repository.IComputadorRepositorio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of the {@link IComputadorRepositorio} interface.
 * Handles persistence logic for computer records in the local database.
 */
public class ComputadorDAO implements IComputadorRepositorio {
    private static final String INSERT = "INSERT INTO computadores (numero, status, tier, preco_hora) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, numero, status, tier, preco_hora FROM computadores";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_BY_NUMERO = SELECT_ALL + " WHERE numero = ?";
    private static final String SELECT_LIVRES = SELECT_ALL + " WHERE status = 'LIVRE'";
    private static final String UPDATE = "UPDATE computadores SET numero = ?, status = ?, tier = ?, preco_hora = ? WHERE id = ?";
    private static final String UPDATE_STATUS = "UPDATE computadores SET status = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM computadores WHERE id = ?";

    /**
     * Saves a new computer to the database.
     *
     * @param computador The computer entity to persist.
     * @return The auto-generated ID of the new record, or -1 if insertion failed.
     * @throws RuntimeException if a database access error occurs.
     */
    public int salvar(Computador computador) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            
            pstmt.setInt(1, computador.getNumero());
            pstmt.setString(2, computador.getStatus().name());
            pstmt.setString(3, computador.getTier().name());
            pstmt.setDouble(4, computador.getPrecoHora());
            
            if (pstmt.executeUpdate() > 0) {
                return DatabaseUtils.getLastInsertId(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar computador no banco: " + e.getMessage(), e);
        }
        return -1;
    }

    /**
     * Retrieves all computer records from the database.
     *
     * @return A list of all {@link Computador} objects found.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Searches for a computer by its database ID.
     *
     * @param id The unique identifier of the computer.
     * @return The {@link Computador} instance if found, or null otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Searches for a computer by its logical machine number.
     *
     * @param numero The machine number assigned to the computer.
     * @return The {@link Computador} instance if found, or null otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Lists all computers currently marked as available (LIVRE).
     *
     * @return A list of available {@link Computador} objects.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Updates an existing computer's data.
     *
     * @param computador The computer object containing updated information.
     * @return true if the update was successful, false otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Updates only the status of a specific computer.
     *
     * @param id The ID of the computer to update.
     * @param novoStatus The new status to apply.
     * @return true if the status was updated, false otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
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

    /**
     * Removes a computer record from the database.
     *
     * @param id The ID of the computer to delete.
     * @return true if the record was deleted, false otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
    public boolean deletar(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar computador do banco: " + e.getMessage(), e);
        }
    }

    /**
     * Maps a database row to a Computador object.
     *
     * @param rs The result set at the current cursor position.
     * @return A populated Computador instance.
     * @throws SQLException If column mapping fails.
     */
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
