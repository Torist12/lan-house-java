package com.lanhouse;

import com.lanhouse.controller.AppController;
import com.lanhouse.dao.ClienteDAO;
import com.lanhouse.dao.ComputadorDAO;
import com.lanhouse.dao.DatabaseConnection;
import com.lanhouse.dao.LocacaoDAO;
import com.lanhouse.service.AppService;
import com.lanhouse.service.CalculoValorService;
import com.lanhouse.ui.CliView;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA LAN HOUSE ===\n");

        // Inicializar banco de dados
        DatabaseConnection.criarTabelas();

        var computadorDAO = new ComputadorDAO();
        var clienteDAO = new ClienteDAO();
        var locacaoDAO = new LocacaoDAO();
        var calculoService = new CalculoValorService();

        var service = new AppService(computadorDAO, clienteDAO, locacaoDAO, calculoService);
        var view = new CliView();
        var controller = new AppController(service, view);

        controller.executar();
    }
}