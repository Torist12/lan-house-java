package com.lanhouse.repository.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:lanhouse.db";
    
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
