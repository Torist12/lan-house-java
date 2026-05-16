package com.lanhouse.controller;

import com.lanhouse.service.AppService;
import com.lanhouse.ui.IView;
import com.lanhouse.model.*;

/**
 * Controller que implementa os casos de uso da aplicação.
 * Orquestra a comunicação entre View e Service (injeção de dependência).
 */
public class AppController {
    private final AppService service;
    private final IView view;
    private boolean executando;

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

    private void listarClientes() {
        view.exibirClientes(service.listarClientes());
    }

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

    private void atualizarCliente() {
        int id = view.lerInteiro("ID do cliente: ");
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

    private void deletarCliente() {
        int id = view.lerInteiro("ID do cliente a deletar: ");

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

    private void menuComputadores() {
        while (true) {
            view.exibirMenuComputadores();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> listarComputadores();
                case 2 -> listarComputadoresLivres();
                case 3 -> cadastrarComputador();
                case 4 -> atualizarPrecoComputador();
                case 5 -> deletarComputador();
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    private void listarComputadores() {
        view.exibirComputadores(service.listarComputadores());
    }

    private void listarComputadoresLivres() {
        view.exibirComputadores(service.listarComputadoresLivres());
    }

    private void cadastrarComputador() {
        int numero = view.lerInteiro("Número da máquina: ");
        view.exibirMensagem("Tiers: 1-BASICO, 2-INTERMEDIARIO, 3-GAMER");
        int tierOpcao = view.lerInteiro("Opção de Tier: ");
        double preco = view.lerDouble("Preço por hora: ");

        TierComputador tier = switch (tierOpcao) {
            case 2 -> TierComputador.INTERMEDIARIO;
            case 3 -> TierComputador.GAMER;
            default -> TierComputador.BASICO;
        };

        try {
            int id = service.criarComputador(numero, tier, preco);
            view.exibirMensagem("Computador cadastrado! ID: " + id);
        } catch (Exception e) {
            view.exibirErro(e.getMessage());
        }
    }

    private void atualizarPrecoComputador() {
        int id = view.lerInteiro("ID do computador: ");
        double novoPreco = view.lerDouble("Novo preço por hora: ");
        
        if (service.atualizarComputador(id, 0, null, novoPreco)) {
            view.exibirMensagem("Preço atualizado com sucesso!");
        } else {
            view.exibirErro("Computador não encontrado.");
        }
    }

    private void deletarComputador() {
        int id = view.lerInteiro("ID do computador a remover: ");
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

    private Cliente selecionarCliente() {
        var clientes = service.listarClientes();
        if (clientes.isEmpty()) {
            view.exibirErro("Nenhum cliente cadastrado. Cadastre um cliente antes de iniciar a locação.");
            return null;
        }

        view.exibirClientes(clientes);
        int clienteId = view.lerInteiro("ID do cliente: ");
        Cliente cliente = service.buscarClienteId(clienteId);

        if (cliente == null) {
            view.exibirErro("Cliente não encontrado!");
            return null;
        }

        return cliente;
    }

    private Computador selecionarComputadorLivre() {
        var computadores = service.listarComputadoresLivres();
        if (computadores.isEmpty()) {
            view.exibirErro("Nenhum computador livre disponível no momento.");
            return null;
        }

        view.exibirComputadores(computadores);
        int computadorId = view.lerInteiro("ID do computador: ");
        Computador computador = service.buscarComputadorId(computadorId);

        if (computador == null || computador.getStatus() != StatusComputador.LIVRE) {
            view.exibirErro("Computador inválido ou não disponível!");
            return null;
        }

        return computador;
    }

    private void finalizarLocacao() {
        view.exibirLocacoes(service.listarLocacoesAtivas());

        int locacaoId = view.lerInteiro("ID da locação a finalizar: ");

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

    private void listarLocacoesAtivas() {
        view.exibirLocacoes(service.listarLocacoesAtivas());
    }

    private void historicoCliente() {
        var clientes = service.listarClientes();
        if (clientes.isEmpty()) {
            view.exibirErro("Nenhum cliente registrado!");
            return;
        }

        view.exibirClientes(clientes);
        int clienteId = view.lerInteiro("ID do cliente: ");
        Cliente cliente = service.buscarClienteId(clienteId);

        if (cliente == null) {
            view.exibirErro("Cliente não encontrado!");
            return;
        }

        view.exibirCliente(cliente);
        view.exibirLocacoes(service.listarLocacoesCliente(clienteId));
    }

    // ===== MENU RELATÓRIOS =====

    private void menuRelatorios() {
        while (true) {
            view.exibirMenuRelatorios();
            int opcao = view.lerOpcaoMenu();

            switch (opcao) {
                case 1 -> todasLocacoes();
                case 2 -> faturamentoTotal();
                case 0 -> {
                    return;
                }
                default -> view.exibirErro("Opção inválida!");
            }
        }
    }

    private void todasLocacoes() {
        try {
            view.exibirLocacoes(service.listarTodasLocacoes());
        } catch (RuntimeException e) {
            view.exibirErro(e.getMessage());
        }
    }

    private void faturamentoTotal() {
        double total = service.calcularFaturamentoTotal();
        view.exibirMensagem("FATURAMENTO TOTAL ACUMULADO: R$ " + String.format("%.2f", total));
    }
}
