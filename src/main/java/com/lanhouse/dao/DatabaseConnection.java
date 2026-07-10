package com.lanhouse.dao;

import com.lanhouse.util.PasswordUtils;
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
                status TEXT DEFAULT 'livre',
                tier TEXT,
                preco_hora REAL
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
        
        String sqlLocacoes = """
            CREATE TABLE IF NOT EXISTS locacoes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id INTEGER NOT NULL,
                computador_id INTEGER NOT NULL,
                inicio DATETIME NOT NULL,
                fim DATETIME,
                valor_total DECIMAL(10,2),
                status TEXT DEFAULT 'ativa'
            )
        """;

        String sqlFuncionarios = """
            CREATE TABLE IF NOT EXISTS funcionarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT NOT NULL UNIQUE,
                senha TEXT NOT NULL
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlComputadores);
            stmt.execute(sqlClientes);
            stmt.execute(sqlLocacoes);
            stmt.execute(sqlFuncionarios);
            String admHash = PasswordUtils.hashPassword("adm");
            stmt.execute("INSERT OR IGNORE INTO funcionarios (usuario, senha) VALUES ('adm', '" + admHash + "')");
            
            // Inserir 5 computadores padrão com tier e preço
            String sqlInsert = "INSERT OR IGNORE INTO computadores (numero, tier, preco_hora) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setInt(1, 1);
                pstmt.setString(2, "BASICO");
                pstmt.setDouble(3, 10.00);
                pstmt.addBatch();
                
                pstmt.setInt(1, 2);
                pstmt.setString(2, "BASICO");
                pstmt.setDouble(3, 10.00);
                pstmt.addBatch();
                
                pstmt.setInt(1, 3);
                pstmt.setString(2, "INTERMEDIARIO");
                pstmt.setDouble(3, 15.00);
                pstmt.addBatch();
                
                pstmt.setInt(1, 4);
                pstmt.setString(2, "INTERMEDIARIO");
                pstmt.setDouble(3, 15.00);
                pstmt.addBatch();
                
                pstmt.setInt(1, 5);
                pstmt.setString(2, "GAMER");
                pstmt.setDouble(3, 25.00);
                pstmt.addBatch();
                
                pstmt.executeBatch();
            }

            System.out.println("Banco de dados criado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
