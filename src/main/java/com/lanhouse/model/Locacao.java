package com.lanhouse.model;

import java.time.LocalDateTime;
import java.time.Duration;
/**
 * Representa uma locação (sessão de uso) de um computador por um cliente na Lan House.
 * Represents a rental (usage session) of a computer by a client in the Lan House. Contains information about the start, end, total value, and status of the rental.
 *
 */

public class Locacao {
    private int id;
    private int clienteId;
    private int computadorId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private double valorTotal;
    private StatusLocacao status;

    /**
     * Full constructor to create a Locacao object with all attributes, typically used when retrieving data from the database.
     * @param id The unique identifier of the rental.
     * @param clienteId The ID of the client who made the rental.
     * @param computadorId O ID do computador utilizado na locação.
     * @param inicio O timestamp de início da locação.
     * @param fim O timestamp de fim da locação (pode ser null se ativa).
     * @param valorTotal O valor total calculado da locação.
     * @param status O status atual da locação (ATIVA, FINALIZADA).
     */
    public Locacao(int id, int clienteId, int computadorId, LocalDateTime inicio, LocalDateTime fim, double valorTotal, StatusLocacao status) {
        this.id = id;
        this.clienteId = clienteId;
        this.computadorId = computadorId;
        this.inicio = inicio;
        this.fim = fim;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    /**
     * Constructor to start a new Locacao, without ID, end time, or total value defined. The initial status is always ACTIVE.
     * @param computadorId The ID of the computer to be used.
     * @param inicio The start timestamp of the rental.
     */
    public Locacao(int clienteId, int computadorId, LocalDateTime inicio) {
        this.clienteId = clienteId;
        this.computadorId = computadorId;
        this.inicio = inicio;
        this.status = StatusLocacao.ATIVA;
    }

    // Getters
    /**
     * @return The rental ID.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The client ID.
     */
    public int getClienteId() {
        return clienteId;
    }

    /**
     * @return The computer ID.
     */
    public int getComputadorId() {
        return computadorId;
    }

    /**
     * @return The {@link LocalDateTime} of the start.
     */
    public LocalDateTime getInicio() {
        return inicio;
    }

    /**
     * @return The {@link LocalDateTime} of the end, or null.
     */
    public LocalDateTime getFim() {
        return fim;
    }

    /**
     * @return The total value of the rental.
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     * @return The {@link StatusLocacao} of the rental.
     */
    public StatusLocacao getStatus() {
        return status;
    }

    // Setters
    /**
     * Sets the rental ID. This method is typically used by DAOs after persistence to assign the database-generated ID.
     * @param id The new rental ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the end timestamp of the rental.
     * @param fim The {@link LocalDateTime} of the end.
     */
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    /**
     * Sets the total calculated value for the rental.
     * @param valorTotal The new total value.
     */
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * Sets the status of the rental.
     * @param status The new {@link StatusLocacao}.
     */
    public void setStatus(StatusLocacao status) {
        this.status = status;
    }

    // Métodos úteis
    /**
     * Calculates the duration of the rental in minutes. Returns 0 if the rental has not yet ended (end is null).
     * @return The duration in minutes.
     */
    public long getTempoMinutos() {
        if (fim == null) return 0;
        return Duration.between(inicio, fim).toMinutes();
    }

    /**
     * Calculates the duration of the rental in hours. Returns 0 if the rental has not yet ended (end is null).
     * @return The duration in hours.
     */
    public double getTempoHoras() {
        return getTempoMinutos() / 60.0;
    }

    @Override
    /**
     */
    public String toString() {
        return "Locacao{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", computadorId=" + computadorId +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", valorTotal=" + valorTotal +
                ", status=" + status +
                '}';
    }
}
