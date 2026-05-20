package com.lanhouse.controller;

import com.lanhouse.service.AppService;
import com.lanhouse.ui.IView;
import com.lanhouse.model.*;

/**
 * Controller that implements the application's use cases.
 * Orchestrates communication between the View and Service layers (dependency injection).
 */
public class AppController {
    private final AppService service;
    private final IView view;
    private boolean executando;

    /**
     * Constructs the controller with necessary dependencies.
     *
     * @param service The application service layer.
     * @param view The UI view implementation.
     */
    public AppController(AppService service, IView view) {
        this.service = service;
        this.view = view;
        this.executando = true;
    }

    /**
     * Loop principal da aplicação
     */
    public void executar() {
        while (executando) {
            view.exibirMenuPrincipal();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuComputadores();
                case 3 -> menuLocacoes();
                case 4 -> menuRelatorios();
                case 0 -> {
                    view.exibirMensagem("Encerrando aplicação...");
                    executando = false;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
        view.encerrar();
    }

    // ===== MENU CLIENTES =====

    /**
     * Displays and handles the client management submenu.
     */
    private void menuClientes() {
        while (true) {
            view.exibirMenuClientes();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> listarClientes();
                case 2 -> registrarCliente();
                case 3 -> atualizarCliente();
                case 4 -> deletarCliente();
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    /**
     * Fetches and displays all registered clients.
     */
    private void listarClientes() {
        view.exibirClientes(service.listarClientes());
    }

    /**
     * Guides the user through registering a new client.
     */
    private void registrarCliente() {
        String nome = view.lerTexto("Nome do cliente: ");
        String documento = view.lerTexto("Documento (CPF/CNPJ): ");
        String telefone = view.lerTexto("Telefone: ");

        try {
            int id = service.criarCliente(nome, documento, telefone);
            if (id > 0) {
                view.exibirMensagem("Cliente registrado com sucesso! ID: " + id);
            }
        } catch (RuntimeException e) {
            view.exibirErro("Não foi possível registrar o cliente: " + e.getMessage());
        }
    }

    /**
     * Guides the user through updating an existing client's information.
     */
    private void atualizarCliente() {
        int id = view.lerInteiro("ID do cliente: ");
        if (id == -1) {
            view.exibirErro("ID do cliente inválido.");
            return;
        }

        Cliente cliente = service.buscarClienteId(id);

        if (cliente == null) {
            view.exibirErro("Cliente não encontrado!");
            return;
        }

        view.exibirCliente(cliente);
        String novoNome = view.lerTexto("Novo nome (Enter para manter): ");
        String novoTelefone = view.lerTexto("Novo telefone (Enter para manter): ");

        if (novoNome.isEmpty()) novoNome = cliente.getNome();
        if (novoTelefone.isEmpty()) novoTelefone = cliente.getTelefone();

        try {
            if (service.atualizarCliente(id, novoNome, novoTelefone)) {
                view.exibirMensagem("Cliente atualizado com sucesso!");
            }
        } catch (RuntimeException e) {
            view.exibirErro("Erro ao atualizar dados: " + e.getMessage());
        }
    }

    /**
     * Guides the user through deleting a client record.
     */
    private void deletarCliente() {
        int id = view.lerInteiro("ID do cliente a deletar: ");
        if (id == -1) {
            view.exibirErro("ID do cliente inválido.");
            return;
        }


        if (view.confirmar("Tem certeza que deseja deletar este cliente?")) {
            try {
                if (service.deletarCliente(id)) {
                    view.exibirMensagem("Cliente deletado com sucesso!");
                } else {
                    view.exibirErro("Não foi possível encontrar um cliente com o ID informado.");
                }
            } catch (IllegalArgumentException e) {
                view.exibirErro(e.getMessage());
            } catch (RuntimeException e) {
                view.exibirErro("Erro técnico ao deletar cliente: " + e.getMessage());
            }
        }
    }

    // ===== MENU COMPUTADORES =====

    /**
     * Displays and handles the computer management submenu.
     */
    private void menuComputadores() {
        while (true) {
            view.exibirMenuComputadores();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> listarComputadores();
                case 2 -> listarComputadoresLivres();
                case 3 -> cadastrarComputador();
                case 4 -> atualizarComputador();
                case 5 -> deletarComputador();
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    /**
     * Fetches and displays all computers.
     */
    private void listarComputadores() {
        view.exibirComputadores(service.listarComputadores());
    }

    /**
     * Fetches and displays only available computers.
     */
    private void listarComputadoresLivres() {
        view.exibirComputadores(service.listarComputadoresLivres());
    }

    /**
     * Guides the user through registering a new computer.
     */
    private void cadastrarComputador() {
        int numero = view.lerInteiro("Número da máquina: ");
        if (numero == -1) {
            view.exibirErro("Número da máquina inválido.");
            return;
        }

        view.exibirMensagem("Tiers: 1-BASICO, 2-INTERMEDIARIO, 3-GAMER");
        int tierOpcao = view.lerInteiro("Opção de Tier: ");
        double preco = view.lerDouble("Preço por hora: ");

        TierComputador tier = switch (tierOpcao) {
            case 2 -> TierComputador.INTERMEDIARIO;
            case 3 -> TierComputador.GAMER;
            default -> TierComputador.BASICO;
        };

        try {
            int id = service.criarComputador(numero, tier, preco); // Service will validate numero and preco
            if (id > 0) {
                view.exibirMensagem("Computador cadastrado! ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            view.exibirErro(e.getMessage());
        }
    }

    /**
     * Guides the user through updating the details of a computer.
     */
    private void atualizarComputador() {
        int id = view.lerInteiro("ID do computador: ");
        if (id == -1) {
            view.exibirErro("ID do computador inválido.");
            return;
        }

        Computador pc = service.buscarComputadorId(id);

        if (pc == null) {
            view.exibirErro("Computador não encontrado.");
            return;
        }

        view.exibirComputador(pc);

        // Read new number, validate, and use old if invalid or 0
        int novoNumero = view.lerInteiro("Novo número (0 para manter): ");
        if (novoNumero == -1) { // User entered non-numeric input
            view.exibirErro("Número da máquina inválido. Mantendo o número anterior.");
            novoNumero = pc.getNumero();
        } else if (novoNumero == 0) { // User explicitly chose to keep old number
            novoNumero = pc.getNumero();
        }

        view.exibirMensagem("Tiers: 1-BASICO, 2-INTERMEDIARIO, 3-GAMER, 0-Manter");
        int tierOpcao = view.lerInteiro("Opção de Tier: ");
        TierComputador novoTier = switch (tierOpcao) {
            case 1 -> TierComputador.BASICO;
            case 2 -> TierComputador.INTERMEDIARIO;
            case 3 -> TierComputador.GAMER;
            default -> pc.getTier();
        };

        // Read new price, validate, and use old if invalid or -1
        double novoPreco = view.lerDouble("Novo preço por hora (-1 para manter): ");
        if (novoPreco == -1.0) { // User entered non-numeric input or explicitly chose to keep old price
            novoPreco = pc.getPrecoHora();
        } else if (novoPreco < 0) { // User entered a negative price
            view.exibirErro("Preço por hora não pode ser negativo. Mantendo o preço anterior.");
            novoPreco = pc.getPrecoHora();
        }

        try {
            if (service.atualizarComputador(id, novoNumero, novoTier, novoPreco)) {
                view.exibirMensagem("Computador atualizado com sucesso!");
            } else {
                view.exibirErro("Computador não encontrado."); // Should ideally be caught by IllegalArgumentException if ID is valid
            }
        } catch (IllegalArgumentException e) {
            view.exibirErro("Erro ao atualizar computador: " + e.getMessage());
        }
    }

    /**
     * Guides the user through removing a computer record.
     */
    private void deletarComputador() {
        int id = view.lerInteiro("ID do computador a remover: ");
        if (id == -1) {
            view.exibirErro("ID do computador inválido.");
            return;
        }

        if (view.confirmar("Deseja realmente remover esta máquina?")) {
            try {
                if (service.deletarComputador(id)) {
                    view.exibirMensagem("Computador removido.");
                } else {
                    view.exibirErro("ID não encontrado.");
                }
            } catch (Exception e) {
                view.exibirErro(e.getMessage());
            }
        }
    }

    // ===== MENU LOCAÇÕES =====

    /**
     * Displays and handles the rental management submenu.
     */
    private void menuLocacoes() {
        while (true) {
            view.exibirMenuLocacoes();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> iniciarLocacao();
                case 2 -> finalizarLocacao();
                case 3 -> listarLocacoesAtivas();
                case 4 -> historicoCliente();
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    /**
     * Guides the user through starting a new computer rental session.
     */
    private void iniciarLocacao() {
        Cliente cliente = selecionarCliente();
        if (cliente == null) {
            return;
        }

        Computador computador = selecionarComputadorLivre();
        if (computador == null) {
            return;
        }

        try {
            int locacaoId = service.iniciarLocacao(cliente.getId(), computador.getId());
            view.exibirMensagem("Locação iniciada! ID: " + locacaoId);
            view.exibirMensagem(service.descricaoComputador(computador));
        } catch (IllegalArgumentException e) {
            view.exibirErro(e.getMessage());
        } catch (RuntimeException e) {
            view.exibirErro("Falha técnica ao acessar o banco de dados: " + e.getMessage());
        }
    }

    /**
     * Helper method to interactively select a client.
     *
     * @return The selected {@link Cliente} or null if selection failed.
     */
    private Cliente selecionarCliente() {
        var clientes = service.listarClientes();
        if (clientes.isEmpty()) {
            view.exibirErro("Nenhum cliente cadastrado. Cadastre um cliente antes de iniciar a locação.");
            return null;
        }

        view.exibirClientes(clientes);
        int clienteId = view.lerInteiro("ID do cliente: ");
        if (clienteId == -1) {
            view.exibirErro("ID do cliente inválido.");
            return null; // Corrected: must return Cliente
        }
        Cliente cliente = service.buscarClienteId(clienteId);

        if (cliente == null) {
            view.exibirErro("Cliente não encontrado!");
            return null;
        }

        return cliente;
    }

    /**
     * Helper method to interactively select an available computer.
     *
     * @return The selected {@link Computador} or null if selection failed.
     */
    private Computador selecionarComputadorLivre() {
        var computadores = service.listarComputadoresLivres();
        if (computadores.isEmpty()) {
            view.exibirErro("Nenhum computador livre disponível no momento.");
            return null;
        }

        view.exibirComputadores(computadores);
        int computadorId = view.lerInteiro("ID do computador: ");
        if (computadorId == -1) {
            view.exibirErro("ID do computador inválido.");
            return null; // Corrected: already returning null, but removed duplicate declaration
        }
        Computador computador = service.buscarComputadorId(computadorId);

        if (computador == null || computador.getStatus() != StatusComputador.LIVRE) {
            view.exibirErro("Computador inválido ou não disponível!");
            return null;
        }

        return computador;
    }

    /**
     * Guides the user through ending an active rental session and calculating the total.
     */
    private void finalizarLocacao() {
        view.exibirLocacoes(service.listarLocacoesAtivas());

        int locacaoId = view.lerInteiro("ID da locação a finalizar: ");
        if (locacaoId == -1) {
            view.exibirErro("ID da locação inválido.");
            return;
        }

        try {
            double valor = service.finalizarLocacao(locacaoId);
            view.exibirMensagem("Locação finalizada!");
            view.exibirMensagem("Valor a pagar: R$ " + String.format("%.2f", valor));
        } catch (IllegalArgumentException e) {
            view.exibirErro(e.getMessage());
        } catch (RuntimeException e) {
            view.exibirErro("Erro ao salvar finalização no banco: " + e.getMessage());
        }
    }

    /**
     * Displays all currently active rentals.
     */
    private void listarLocacoesAtivas() {
        view.exibirLocacoes(service.listarLocacoesAtivas());
    }

    /**
     * Displays the rental history for a specific client.
     */
    private void historicoCliente() {
        var clientes = service.listarClientes();
        if (clientes.isEmpty()) {
            view.exibirErro("Nenhum cliente registrado!");
            return;
        }

        view.exibirClientes(clientes);
        int clienteId = view.lerInteiro("ID do cliente: ");
        if (clienteId == -1) {
            view.exibirErro("ID do cliente inválido.");
            return; // Corrected: removed duplicate declaration
        }
        Cliente cliente = service.buscarClienteId(clienteId);

        if (cliente == null) {
            view.exibirErro("Cliente não encontrado!");
            return;
        }

        view.exibirCliente(cliente);
        view.exibirLocacoes(service.listarLocacoesCliente(clienteId));
    }

    // ===== MENU RELATÓRIOS =====

    /**
     * Displays and handles the reports submenu.
     */
    private void menuRelatorios() {
        while (true) {
            view.exibirMenuRelatorios();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> todasLocacoes();
                case 2 -> faturamentoTotal();
                // Option 2 for faturamentoTotal() was missing in the menu display
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    /**
     * Displays a log of all rental transactions.
     */
    private void todasLocacoes() {
        try {
            view.exibirLocacoes(service.listarTodasLocacoes());
        } catch (RuntimeException e) {
            view.exibirErro(e.getMessage());
        }
    }

    /**
     * Displays the accumulated revenue of the system.
     */
    private void faturamentoTotal() {
        double total = service.calcularFaturamentoTotal();
        view.exibirMensagem("FATURAMENTO TOTAL ACUMULADO: R$ " + String.format("%.2f", total));
    }
}
