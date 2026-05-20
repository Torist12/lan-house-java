package com.lanhouse.service;

import com.lanhouse.repository.IClienteRepositorio;
import com.lanhouse.repository.IComputadorRepositorio;
import com.lanhouse.repository.ILocacaoRepositorio;
import com.lanhouse.model.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer that manages the application state.
 * Responsible for entity CRUD and business operations.
 */
public class AppService {
    private final IComputadorRepositorio computadorDAO;
    private final IClienteRepositorio clienteDAO;
    private final ILocacaoRepositorio locacaoDAO;
    private final CalculoValorService calculoService;

    /**
     * Constructs an {@code AppService} with the necessary data access objects and calculation service.
     *
     * @param computadorDAO The data access object for computer operations.
     * @param clienteDAO The data access object for client operations.
     * @param locacaoDAO The data access object for rental operations.
     * @param calculoService The service responsible for value calculations.
     */

    public AppService(IComputadorRepositorio computadorDAO, IClienteRepositorio clienteDAO,
                      ILocacaoRepositorio locacaoDAO, CalculoValorService calculoService) {
        this.computadorDAO = computadorDAO;
        this.clienteDAO = clienteDAO;
        this.locacaoDAO = locacaoDAO;
        this.calculoService = calculoService;
    }

    // ===== CLIENT OPERATIONS =====

    /**
     * Creates and persists a new client.
     *
     * @param nome The name of the client.
     * @param documento The unique identification document.
     * @param telefone Contact number.
     * @throws IllegalArgumentException if name or document are blank.
     * @return The ID of the created client.
     */
    public int criarCliente(String nome, String documento, String telefone) {
        if (nome == null || nome.isBlank() || documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Nome e documento são obrigatórios.");
        }
        
        Cliente cliente = new Cliente(nome, documento, telefone);
        return clienteDAO.salvar(cliente);
    }

    /**
     * Lists all registered clients.
     *
     * @return A list of {@link Cliente}.
     */
    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    /**
     * Finds a client by ID.
     *
     * @param id The client ID.
     * @return The found {@link Cliente} or null.
     */
    public Cliente buscarClienteId(int id) {
        return clienteDAO.buscarPorId(id);
    }

    /**
     * Finds a client by document.
     *
     * @param documento The document string.
     * @return The found {@link Cliente} or null.
     */
    public Cliente buscarClienteDocumento(String documento) {
        return clienteDAO.buscarPorDocumento(documento);
    }

    /**
     * Updates client details.
     *
     * @return true if successful.
     */
    public boolean atualizarCliente(int id, String nome, String telefone) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente != null) {
            cliente.setNome(nome);
            cliente.setTelefone(telefone);
            return clienteDAO.atualizar(cliente);
        }
        return false;
    }

    /**
     * Deletes a client if they have no active rentals.
     *
     * @param id The ID to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws IllegalArgumentException if the client has active sessions.
     */
    public boolean deletarCliente(int id) {
        // Business validation: Do not allow deleting a client with an active rental
        boolean temLocacaoAtiva = locacaoDAO.listarAtivas().stream()
                .anyMatch(l -> l.getClienteId() == id);
        if (temLocacaoAtiva) {
            throw new IllegalArgumentException("Não é possível deletar um cliente que possui uma locação ativa.");
        }
        return clienteDAO.deletar(id);
    }

    // ===== COMPUTER OPERATIONS =====

    /**
     * Registers a new computer.
     *
     * @throws IllegalArgumentException if the machine number is already taken.
     * @throws IllegalArgumentException if the machine number is not positive or price is negative.
     * @return The ID of the created computer.
     */
    public int criarComputador(int numero, TierComputador tier, double precoHora) {
        if (numero <= 0 || precoHora < 0) {
            throw new IllegalArgumentException("Número da máquina deve ser positivo e preço não pode ser negativo.");
        }
        
        if (computadorDAO.buscarPorNumero(numero) != null) {
            throw new IllegalArgumentException("Já existe um computador com este número.");
        }
        Computador pc = new Computador(numero, StatusComputador.LIVRE, tier, precoHora);
        return computadorDAO.salvar(pc);
    }

    /**
     * Updates an existing computer's details.
     * This method updates the machine number, tier, and hourly price of the computer.
     *
     * @param id The ID of the computer to update.
     * @param numero The new machine number.
     * @param tier The new tier.
     * @param precoHora The new hourly price for the computer.
     * @throws IllegalArgumentException if update data is invalid (e.g., non-positive number, negative price).
     * @throws IllegalArgumentException if the new machine number is already in use by another computer.
     * @return true if the update was successful, false otherwise.
     */
    public boolean atualizarComputador(int id, int numero, TierComputador tier, double precoHora) {
        if (numero <= 0 || precoHora < 0) {
            throw new IllegalArgumentException("Dados de atualização inválidos.");
        }

        Computador pc = computadorDAO.buscarPorId(id);
        if (pc == null) return false;
        
        // Validação: Se o número mudou, verificar se o novo número já existe em outro ID
        Computador existente = computadorDAO.buscarPorNumero(numero);
        if (existente != null && existente.getId() != id) {
            throw new IllegalArgumentException("O número de máquina " + numero + " já está em uso por outro computador.");
        }

        pc.setNumero(numero);
        pc.setTier(tier);
        pc.setPrecoHora(precoHora);
        return computadorDAO.atualizar(pc);
    }

    /**
     * Lists all computers.
     *
     * @return A list of all registered computers.
     */
    public List<Computador> listarComputadores() {
        return computadorDAO.listarTodos();
    }

    /**
     * Lists only available computers.
     *
     * @return A list of computers with status LIVRE.
     */
    public List<Computador> listarComputadoresLivres() {
        return computadorDAO.listarLivres();
    }

    /**
     * Finds a computer by ID.
     *
     * @param id The computer unique ID.
     * @return The computer if found, otherwise null.
     */
    public Computador buscarComputadorId(int id) {
        return computadorDAO.buscarPorId(id);
    }

    /**
     * Finds a computer by its assigned machine number.
     *
     * @param numero The logical number of the machine.
     * @return The computer if found, otherwise null.
     */
    public Computador buscarComputadorNumero(int numero) {
        return computadorDAO.buscarPorNumero(numero);
    }

    /**
     * Deletes a computer record if it is not currently occupied.
     *
     * @param id The ID of the computer to delete.
     * @return true if deletion was successful.
     * @throws IllegalArgumentException if the machine is in use.
     */
    public boolean deletarComputador(int id) {
        Computador pc = computadorDAO.buscarPorId(id);
        if (pc != null && pc.getStatus() != StatusComputador.LIVRE) {
            throw new IllegalArgumentException("Não é possível remover um computador que está ocupado.");
        }
        return computadorDAO.deletar(id);
    }

    // ===== RENTAL OPERATIONS =====

    /**
     * Starts a rental session.
     *
     * @param clienteId The ID of the client renting.
     * @param computadorId The ID of the machine being rented.
     * @return The created rental ID.
     * @throws IllegalArgumentException if client or computer not found, computer not available,
     *                                  or client already has an active rental.
     * @throws RuntimeException if a database access error occurs during the operation.
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
     * Finalizes a rental session, calculates the total price, and frees the computer.
     *
     * @param locacaoId The ID of the session to end.
     * @return The final total value calculated.
     * @throws IllegalArgumentException if the rental is not found or not active,
     *                                  or if the associated computer is not found.
     * @throws RuntimeException if a database access error occurs during the operation.
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

    /**
     * Returns a list of all rentals that haven't been finished.
     *
     * @return List of active rentals.
     * @throws RuntimeException if a database access error occurs.
     */
    public List<Locacao> listarLocacoesAtivas() {
        return locacaoDAO.listarAtivas();
    }

    /**
     * Returns all rental history for a client.
     *
     * @param clienteId The ID of the client to filter by.
     * @throws RuntimeException if a database access error occurs.
     * @return List of rentals for the specified client.
     */
    public List<Locacao> listarLocacoesCliente(int clienteId) {
        return locacaoDAO.listarPorCliente(clienteId);
    }

    /**
     * Returns every rental record in the system.
     *
     * @return List of all historical and current rentals.
     * @throws RuntimeException if a database access error occurs.
     */
    public List<Locacao> listarTodasLocacoes() {
        return locacaoDAO.listarTodas();
    }

    /**
     * Finds a rental record.
     *
     * @param id The rental ID.
     * @throws RuntimeException if a database access error occurs.
     * @return The rental record or null.
     */
    public Locacao buscarLocacao(int id) {
        return locacaoDAO.buscarPorId(id);
    }

    /**
     * Sums the total value of all completed rentals.
     *
     * @return The total accumulated revenue.
     * @throws RuntimeException if a database access error occurs.
     */
    public double calcularFaturamentoTotal() {
        return locacaoDAO.listarTodas().stream()
                .mapToDouble(Locacao::getValorTotal)
                .sum();
    }

    // ===== CALCULATIONS AND UTILITIES =====

    /**
     * Provides a price estimate based on duration and machine tier.
     *
     * @param tempoMinutos The duration in minutes.
     * @param precoHora The hourly price of the computer.
     * @param tier The tier of the computer.
     * @return The estimated value.
     */
    public double calcularValorEstimado(long tempoMinutos, double precoHora, TierComputador tier) {
        return calculoService.calcularValorEstimado(tempoMinutos, precoHora, tier);
    }

    /**
     * Returns a human-readable description of a computer's pricing structure,
     * including its number, tier, and hourly price.
     *
     * @param computador The computer for which to generate the description.
     */
    public String descricaoComputador(Computador computador) {
        return calculoService.descricaoPreco(computador);
    }
}
