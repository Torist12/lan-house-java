package com.lanhouse.service;

import com.lanhouse.repository.IClienteRepositorio;
import com.lanhouse.repository.IComputadorRepositorio;
import com.lanhouse.repository.ILocacaoRepositorio;
import com.lanhouse.model.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de serviço que gerencia o estado da aplicação.
 * Responsável por CRUD de entidades e operações de negócio.
 */
public class AppService {
    private final IComputadorRepositorio computadorDAO;
    private final IClienteRepositorio clienteDAO;
    private final ILocacaoRepositorio locacaoDAO;
    private final CalculoValorService calculoService;

    public AppService(IComputadorRepositorio computadorDAO, IClienteRepositorio clienteDAO,
                      ILocacaoRepositorio locacaoDAO, CalculoValorService calculoService) {
        this.computadorDAO = computadorDAO;
        this.clienteDAO = clienteDAO;
        this.locacaoDAO = locacaoDAO;
        this.calculoService = calculoService;
    }

    // ===== OPERAÇÕES COM CLIENTE =====

    public int criarCliente(String nome, String documento, String telefone) {
        Cliente cliente = new Cliente(nome, documento, telefone);
        return clienteDAO.salvar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public Cliente buscarClienteId(int id) {
        return clienteDAO.buscarPorId(id);
    }

    public Cliente buscarClienteDocumento(String documento) {
        return clienteDAO.buscarPorDocumento(documento);
    }

    public boolean atualizarCliente(int id, String nome, String telefone) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente != null) {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            return clienteDAO.atualizar(cliente);
        }
        return false;
    }

    public boolean deletarCliente(int id) {
        // Validação de negócio: Não permitir deletar cliente com locação ativa
        boolean temLocacaoAtiva = locacaoDAO.listarAtivas().stream()
                .anyMatch(l -> l.getClienteId() == id);
        if (temLocacaoAtiva) {
            throw new IllegalArgumentException("Não é possível deletar um cliente que possui uma locação ativa.");
        }
        return clienteDAO.deletar(id);
    }

    // ===== OPERAÇÕES COM COMPUTADOR =====

    public int criarComputador(int numero, TierComputador tier, double precoHora) {
        if (computadorDAO.buscarPorNumero(numero) != null) {
            throw new IllegalArgumentException("Já existe um computador com este número.");
        }
        Computador pc = new Computador(numero, StatusComputador.LIVRE, tier, precoHora);
        return computadorDAO.salvar(pc);
    }

    public boolean atualizarComputador(int id, int numero, TierComputador tier, double precoHora) {
        Computador pc = computadorDAO.buscarPorId(id);
        if (pc == null) return false;
        
        pc.setPrecoHora(precoHora);
        // Aqui poderiam ser adicionados setters para numero e tier no modelo se necessário
        return computadorDAO.atualizar(pc);
    }

    public List<Computador> listarComputadores() {
        return computadorDAO.listarTodos();
    }

    public List<Computador> listarComputadoresLivres() {
        return computadorDAO.listarLivres();
    }

    public Computador buscarComputadorId(int id) {
        return computadorDAO.buscarPorId(id);
    }

    public Computador buscarComputadorNumero(int numero) {
        return computadorDAO.buscarPorNumero(numero);
    }

    public boolean deletarComputador(int id) {
        Computador pc = computadorDAO.buscarPorId(id);
        if (pc != null && pc.getStatus() != StatusComputador.LIVRE) {
            throw new IllegalArgumentException("Não é possível remover um computador que está ocupado.");
        }
        return computadorDAO.deletar(id);
    }

    // ===== OPERAÇÕES COM LOCAÇÃO =====

    /**
     * Inicia uma locação verificando se o computador está livre
     */
    public int iniciarLocacao(int clienteId, int computadorId) {
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        Computador pc = computadorDAO.buscarPorId(computadorId);
        if (pc == null) {
            throw new IllegalArgumentException("Computador não encontrado");
        }

        if (pc.getStatus() != StatusComputador.LIVRE) {
            throw new IllegalArgumentException("Computador não está disponível");
        }

        // REGRA DE NEGÓCIO: Impedir que o mesmo cliente tenha mais de uma locação ativa
        boolean jaPossuiAtiva = locacaoDAO.listarAtivas().stream()
                .anyMatch(l -> l.getClienteId() == clienteId);
        if (jaPossuiAtiva) {
            throw new IllegalArgumentException("Este cliente já possui uma locação em andamento.");
        }

        Locacao locacao = new Locacao(clienteId, computadorId, LocalDateTime.now());
        int locacaoId = locacaoDAO.iniciarLocacao(locacao);

        if (locacaoId > 0) {
            computadorDAO.atualizarStatus(computadorId, StatusComputador.OCUPADO);
        }

        return locacaoId;
    }

    /**
     * Finaliza uma locação e calcula o valor total
     */
    public double finalizarLocacao(int locacaoId) {
        Locacao locacao = locacaoDAO.buscarPorId(locacaoId);
        if (locacao == null || locacao.getStatus() != StatusLocacao.ATIVA) {
            throw new IllegalArgumentException("Locação não encontrada ou já finalizada");
        }

        Computador computador = computadorDAO.buscarPorId(locacao.getComputadorId());
        if (computador == null) {
            throw new IllegalArgumentException("Computador não encontrado");
        }

        LocalDateTime fim = LocalDateTime.now();
        locacao.setFim(fim);

        double valorTotal = calculoService.calcularValor(locacao, computador);
        locacao.setValorTotal(valorTotal);
        locacao.setStatus(StatusLocacao.FINALIZADA);

        locacaoDAO.finalizarLocacao(locacaoId, fim, valorTotal);
        computadorDAO.atualizarStatus(locacao.getComputadorId(), StatusComputador.LIVRE);

        return valorTotal;
    }

    public List<Locacao> listarLocacoesAtivas() {
        return locacaoDAO.listarAtivas();
    }

    public List<Locacao> listarLocacoesCliente(int clienteId) {
        return locacaoDAO.listarPorCliente(clienteId);
    }

    public List<Locacao> listarTodasLocacoes() {
        return locacaoDAO.listarTodas();
    }

    public Locacao buscarLocacao(int id) {
        return locacaoDAO.buscarPorId(id);
    }

    public double calcularFaturamentoTotal() {
        return locacaoDAO.listarTodas().stream()
                .mapToDouble(Locacao::getValorTotal)
                .sum();
    }

    // ===== CÁLCULOS E UTILITÁRIOS =====

    public double calcularValorEstimado(long tempoMinutos, double precoHora, TierComputador tier) {
        return calculoService.calcularValorEstimado(tempoMinutos, precoHora, tier);
    }

    public String descricaoComputador(Computador computador) {
        return calculoService.descricaoPreco(computador);
    }
}
