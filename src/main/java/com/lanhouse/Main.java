package com.lanhouse;

import com.lanhouse.dao.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA LAN HOUSE ===\n");
        
        // Criar as tabelas
        DatabaseConnection.criarTabelas();
        
        // Testar conexão usando try-with-resources
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM computadores")) {
            
            System.out.println("Conectado ao banco!");
            
            System.out.println("\n--- Computadores ---");
            while (rs.next()) {
                System.out.println("Máquina " + rs.getInt("numero") + ": " + rs.getString("status"));
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        System.out.println("\nSistema rodando!");
    }
}