package com.lanhouse;

import com.lanhouse.controller.AppController;
import com.lanhouse.dao.ClienteDAO;
import com.lanhouse.dao.ComputadorDAO;
import com.lanhouse.dao.DatabaseConnection;
import com.lanhouse.dao.FuncionarioDAO;
import com.lanhouse.dao.LocacaoDAO;
import com.lanhouse.service.AppService;
import com.lanhouse.service.AuthService;
import com.lanhouse.service.CalculoValorService;
import com.lanhouse.ui.CliView;
import com.lanhouse.ui.LanHouseJavaFxView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
            executarCli();
            return;
        }

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DatabaseConnection.criarTabelas();

        var computadorDAO = new ComputadorDAO();
        var clienteDAO = new ClienteDAO();
        var locacaoDAO = new LocacaoDAO();
        var funcionarioDAO = new FuncionarioDAO();
        var calculoService = new CalculoValorService();

        var service = new AppService(computadorDAO, clienteDAO, locacaoDAO, calculoService, funcionarioDAO);
        var authService = new AuthService(funcionarioDAO);
        new LanHouseJavaFxView(stage, service, authService);
    }

    private static void executarCli() {
        System.out.println("=== SISTEMA LAN HOUSE ===\n");

        DatabaseConnection.criarTabelas();

        var computadorDAO = new ComputadorDAO();
        var clienteDAO = new ClienteDAO();
        var locacaoDAO = new LocacaoDAO();
        var funcionarioDAO = new FuncionarioDAO();
        var calculoService = new CalculoValorService();

        var service = new AppService(computadorDAO, clienteDAO, locacaoDAO, calculoService, funcionarioDAO);
        var view = new CliView();
        var controller = new AppController(service, view);

        controller.executar();
    }
}