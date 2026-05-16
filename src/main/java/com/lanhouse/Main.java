package com.lanhouse;

import com.lanhouse.controller.AppController;
import com.lanhouse.repository.sqlite.ClienteDAO;
import com.lanhouse.repository.sqlite.ComputadorDAO;
import com.lanhouse.repository.sqlite.LocacaoDAO;
import com.lanhouse.service.AppService;
import com.lanhouse.service.CalculoValorService;
import com.lanhouse.ui.CliView;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA LAN HOUSE ===\n");

        // A inicialização do schema é feita automaticamente na primeira conexão.

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