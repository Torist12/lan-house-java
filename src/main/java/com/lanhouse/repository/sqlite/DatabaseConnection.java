package com.lanhouse.repository.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections for the SQLite backend.
 * Configures connection parameters and ensures the schema is prepared.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:lanhouse.db";
    
    /**
     * Establishes a connection to the SQLite database.
     * Also enables foreign key constraints and triggers schema validation.
     *
     * @return A {@link Connection} object to the database.
     * @throws SQLException If the connection fails or schema initialization encounters an error.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // Habilita suporte a chaves estrangeiras no SQLite por conexão
        conn.createStatement().execute("PRAGMA foreign_keys = ON;");
        // Ensure schema exists on first connection
        try {
            SchemaManager.ensureSchema(conn);
        } catch (SQLException e) {
            // If schema creation fails, close connection and rethrow
            try { conn.close(); } catch (Exception ignored) {}
            throw e;
        }
        return conn;
    }
}
