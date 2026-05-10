package com.lanhouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ComputadorDAO {
    
    public void listarComputadores() {
        String sql = "SELECT numero, status FROM computadores";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- Lista de Computadores (via DAO) ---");
            while (rs.next()) {
                int numero = rs.getInt("numero");
                String status = rs.getString("status");
                System.out.printf("Máquina %d: %s%n", numero, status);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar computadores: " + e.getMessage());
        }
    }
}