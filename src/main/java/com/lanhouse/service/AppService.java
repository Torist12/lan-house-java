package com.lanhouse.service;

import com.lanhouse.dao.memory.*;
import com.lanhouse.model.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Camada de serviço que gerencia o estado da aplicação.
 * Responsável por CRUD de entidades e operações de negócio.
 */
public class AppService {
    private final ComputadorDAOMemoria computadorDAO;
    private final ClienteDAOMemoria clienteDAO;
    private final LocacaoDAOMemoria locacaoDAO;
    private final CalculoValorService calculoService;

    public AppService(ComputadorDAOMemoria computadorDAO, ClienteDAOMemoria clienteDAO, 
                      LocacaoDAOMemoria locacaoDAO, CalculoValorService calculoService) {
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
        return clienteDAO.deletar(id);
    }

    // ===== OPERAÇÕES COM COMPUTADOR =====
    
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

        if (!"livre".equals(pc.getStatus())) {
            throw new IllegalArgumentException("Computador não está disponível");
        }

        Locacao locacao = new Locacao(clienteId, computadorId, LocalDateTime.now());
        int locacaoId = locacaoDAO.iniciarLocacao(locacao);

        if (locacaoId > 0) {
            computadorDAO.atualizarStatus(computadorId, "ocupado");
        }

        return locacaoId;
    }

    /**
     * Finaliza uma locação e calcula o valor total
     */
    public double finalizarLocacao(int locacaoId) {
        Locacao locacao = locacaoDAO.buscarPorId(locacaoId);
        if (locacao == null || !"ativa".equals(locacao.getStatus())) {
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
        locacao.setStatus("finalizada");

        locacaoDAO.finalizarLocacao(locacaoId, fim, valorTotal);
        computadorDAO.atualizarStatus(locacao.getComputadorId(), "livre");

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

    // ===== CÁLCULOS E UTILITÁRIOS =====
    
    public double calcularValorEstimado(long tempoMinutos, double precoHora, String tier) {
        return calculoService.calcularValorEstimado(tempoMinutos, precoHora, tier);
    }

    public String descricaoComputador(Computador computador) {
        return calculoService.descricaoPreco(computador);
    }
}
