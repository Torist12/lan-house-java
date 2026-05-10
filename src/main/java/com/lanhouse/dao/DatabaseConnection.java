package com.lanhouse.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:lanhouse.db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void criarTabelas() {
        String sqlComputadores = """
            CREATE TABLE IF NOT EXISTS computadores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero INTEGER NOT NULL UNIQUE,
                status TEXT DEFAULT 'livre'
            )
        """;
        
        String sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                documento TEXT UNIQUE,
                telefone TEXT
            )
        """;
        
        String sqlSessoes = """
            CREATE TABLE IF NOT EXISTS sessoes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                computador_id INTEGER NOT NULL,
                inicio DATETIME NOT NULL,
                fim DATETIME,
                valor_total DECIMAL(10,2),
                status TEXT DEFAULT 'ativa'
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlComputadores);
            stmt.execute(sqlClientes);
            stmt.execute(sqlSessoes);
            
            // Inserir 5 computadores padrão usando PreparedStatement e Batch
            String sqlInsert = "INSERT OR IGNORE INTO computadores (numero) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                for (int i = 1; i <= 5; i++) {
                    pstmt.setInt(1, i);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            System.out.println("Banco de dados criado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
