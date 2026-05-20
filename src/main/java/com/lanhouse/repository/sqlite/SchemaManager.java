package com.lanhouse.repository.sqlite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class responsible for initializing the database schema.
 * Ensures that all required tables exist before the application starts performing operations.
 */
public final class SchemaManager {
    private static volatile boolean initialized = false;

    private SchemaManager() {}

    /**
     * Checks if the schema is initialized and creates the tables if they do not exist.
     *
     * @param conn The database connection used to execute the creation scripts.
     * @throws SQLException If an error occurs during SQL execution.
     */
    static void ensureSchema(Connection conn) throws SQLException {
        if (initialized) return;
        synchronized (SchemaManager.class) {
            if (initialized) return;

            String sqlComputadores = """
                CREATE TABLE IF NOT EXISTS computadores (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    numero INTEGER NOT NULL UNIQUE,
                    status TEXT DEFAULT 'LIVRE',
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
                    status TEXT DEFAULT 'ATIVA',
                    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                    FOREIGN KEY (computador_id) REFERENCES computadores(id)
                )
            """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlComputadores);
                stmt.execute(sqlClientes);
                stmt.execute(sqlLocacoes);
            }

            initialized = true;
        }
    }
}
