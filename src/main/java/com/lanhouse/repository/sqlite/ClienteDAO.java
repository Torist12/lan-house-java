package com.lanhouse.repository.sqlite;

import com.lanhouse.model.Cliente;
import com.lanhouse.repository.IClienteRepositorio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of the {@link IClienteRepositorio} interface.
 * Manages the persistence of client data in the local database.
 */
public class ClienteDAO implements IClienteRepositorio {
    private static final String INSERT = "INSERT INTO clientes (nome, documento, telefone) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, nome, documento, telefone FROM clientes";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
    private static final String SELECT_BY_DOCUMENTO = SELECT_ALL + " WHERE documento = ?";
    private static final String UPDATE = "UPDATE clientes SET nome = ?, telefone = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM clientes WHERE id = ?";

    /**
     * Persists a new client record.
     *
     * @param cliente The client entity to save.
     * @return The auto-generated database ID, or -1 if the operation failed.
     * @throws RuntimeException if a database access error occurs.
     */
    public int salvar(Cliente cliente) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getDocumento());
            pstmt.setString(3, cliente.getTelefone());
            
            if (pstmt.executeUpdate() > 0) {
                return DatabaseUtils.getLastInsertId(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente no banco: " + e.getMessage(), e);
        }
        return -1;
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return A list containing all registered clients.
     * @throws RuntimeException if a database access error occurs.
     */
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                clientes.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    /**
     * Finds a client by their unique ID.
     *
     * @param id The ID to search for.
     * @return The {@link Cliente} instance if found, otherwise null.
     * @throws RuntimeException if a database access error occurs.
     */
    public Cliente buscarPorId(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Finds a client by their unique document (CPF/CNPJ).
     *
     * @param documento The document string to search for.
     * @return The {@link Cliente} instance if found, otherwise null.
     * @throws RuntimeException if a database access error occurs.
     */
    public Cliente buscarPorDocumento(String documento) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_DOCUMENTO)) {
            
            pstmt.setString(1, documento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por documento: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Updates the name and telephone of an existing client.
     *
     * @param cliente The client object containing the ID and updated data.
     * @return true if the record was updated, false otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
    public boolean atualizar(Cliente cliente) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE)) {
            
            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getTelefone());
            pstmt.setInt(3, cliente.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a client record from the database.
     *
     * @param id The unique identifier of the client to remove.
     * @return true if the deletion was successful, false otherwise.
     * @throws RuntimeException if a database access error occurs.
     */
    public boolean deletar(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente do banco: " + e.getMessage(), e);
        }
    }

    /**
     * Maps a database row to a Cliente object.
     *
     * @param rs The result set at the current cursor position.
     * @return A populated Cliente instance.
     * @throws SQLException If column mapping fails.
     */
    private Cliente mapearResultSet(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("documento"),
            rs.getString("telefone")
        );
    }
}
