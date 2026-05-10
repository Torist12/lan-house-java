package com.lanhouse;

import com.lanhouse.controller.AppController;
import com.lanhouse.dao.memory.ClienteDAOMemoria;
import com.lanhouse.dao.memory.ComputadorDAOMemoria;
import com.lanhouse.dao.memory.LocacaoDAOMemoria;
import com.lanhouse.service.AppService;
import com.lanhouse.service.CalculoValorService;
import com.lanhouse.ui.CliView;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA LAN HOUSE ===\n");

        var computadorDAO = new ComputadorDAOMemoria();
        var clienteDAO = new ClienteDAOMemoria();
        var locacaoDAO = new LocacaoDAOMemoria();
        var calculoService = new CalculoValorService();

        var service = new AppService(computadorDAO, clienteDAO, locacaoDAO, calculoService);
        var view = new CliView();
        var controller = new AppController(service, view);

        controller.executar();
    }
}