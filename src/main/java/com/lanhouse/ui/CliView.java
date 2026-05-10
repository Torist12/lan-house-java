package com.lanhouse.ui;

import com.lanhouse.model.*;
import java.util.List;
import java.util.Scanner;

/**
 * Implementação de View em linha de comando (CLI).
 * Interage com o usuário através do console.
 */
public class CliView implements IView {
    private final Scanner scanner;

    public CliView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirMenuPrincipal() {
        limparTela();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║    SISTEMA LAN HOUSE v1.0          ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("\n[1] Gerenciar Clientes");
        System.out.println("[2] Gerenciar Computadores");
        System.out.println("[3] Gerenciar Locações");
        System.out.println("[4] Relatórios");
        System.out.println("[0] Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    @Override
    public void exibirMensagem(String mensagem) {
        System.out.println("\n✓ " + mensagem);
    }

    @Override
    public void exibirErro(String mensagem) {
        System.out.println("\n✗ ERRO: " + mensagem);
    }

    @Override
    public void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public int lerOpcaoMenu() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            return opcao;
        } catch (NumberFormatException e) {
            exibirErro("Digite um número válido!");
            return -1;
        }
    }

    @Override
    public int lerInteiro(String prompt) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            exibirErro("Digite um número inteiro válido!");
            return -1;
        }
    }

    @Override
    public String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    @Override
    public double lerDouble(String prompt) {
        try {
            System.out.print(prompt);
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            exibirErro("Digite um número válido!");
            return -1.0;
        }
    }

    @Override
    public void exibirComputadores(List<Computador> computadores) {
        if (computadores.isEmpty()) {
            exibirMensagem("Nenhum computador registrado!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTA DE COMPUTADORES                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        for (Computador pc : computadores) {
            System.out.printf("ID: %2d | Máq: %d | Status: %-10s | Tier: %-13s | R$%.2f/h%n",
                pc.getId(), pc.getNumero(), pc.getStatus(), pc.getTier(), pc.getPrecoHora());
        }
    }

    @Override
    public void exibirClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            exibirMensagem("Nenhum cliente registrado!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTA DE CLIENTES                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        for (Cliente c : clientes) {
            System.out.printf("ID: %2d | Nome: %-25s | Doc: %s%n",
                c.getId(), c.getNome(), c.getDocumento());
        }
    }

    @Override
    public void exibirLocacoes(List<Locacao> locacoes) {
        if (locacoes.isEmpty()) {
            exibirMensagem("Nenhuma locação encontrada!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTA DE LOCAÇÕES                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        for (Locacao loc : locacoes) {
            System.out.printf("ID: %2d | Cliente: %2d | PC: %2d | Status: %-10s | Total: R$%.2f%n",
                loc.getId(), loc.getClienteId(), loc.getComputadorId(), loc.getStatus(), loc.getValorTotal());
        }
    }

    @Override
    public void exibirComputador(Computador computador) {
        if (computador == null) {
            exibirErro("Computador não encontrado!");
            return;
        }

        System.out.println("\n" + computador);
    }

    @Override
    public void exibirCliente(Cliente cliente) {
        if (cliente == null) {
            exibirErro("Cliente não encontrado!");
            return;
        }

        System.out.println("\n" + cliente);
    }

    @Override
    public void exibirLocacao(Locacao locacao) {
        if (locacao == null) {
            exibirErro("Locação não encontrada!");
            return;
        }

        System.out.println("\n" + locacao);
    }

    @Override
    public void exibirMenuClientes() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     GERENCIAR CLIENTES             ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("[1] Listar clientes");
        System.out.println("[2] Registrar novo cliente");
        System.out.println("[3] Atualizar cliente");
        System.out.println("[4] Deletar cliente");
        System.out.println("[0] Voltar");
        System.out.print("\nEscolha uma opção: ");
    }

    @Override
    public void exibirMenuComputadores() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║    GERENCIAR COMPUTADORES          ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("[1] Listar todos");
        System.out.println("[2] Listar apenas livres");
        System.out.println("[0] Voltar");
        System.out.print("\nEscolha uma opção: ");
    }

    @Override
    public void exibirMenuLocacoes() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║    GERENCIAR LOCAÇÕES              ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("[1] Iniciar locação");
        System.out.println("[2] Finalizar locação");
        System.out.println("[3] Listar locações ativas");
        System.out.println("[4] Listar histórico de cliente");
        System.out.println("[0] Voltar");
        System.out.print("\nEscolha uma opção: ");
    }

    @Override
    public void exibirMenuRelatorios() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      RELATÓRIOS                    ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("[1] Todas as locações");
        System.out.println("[0] Voltar");
        System.out.print("\nEscolha uma opção: ");
    }

    @Override
    public boolean confirmar(String mensagem) {
        System.out.print("\n" + mensagem + " (S/N): ");
        String resposta = scanner.nextLine().trim().toUpperCase();
        return resposta.equals("S") || resposta.equals("SIM");
    }
}
