package com.lanhouse.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Locacao {
    private int id;
    private int clienteId;
    private int computadorId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private double valorTotal;
    private StatusLocacao status;

    public Locacao(int id, int clienteId, int computadorId, LocalDateTime inicio, LocalDateTime fim, double valorTotal, StatusLocacao status) {
        this.id = id;
        this.clienteId = clienteId;
        this.computadorId = computadorId;
        this.inicio = inicio;
        this.fim = fim;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Locacao(int clienteId, int computadorId, LocalDateTime inicio) {
        this.clienteId = clienteId;
        this.computadorId = computadorId;
        this.inicio = inicio;
        this.status = StatusLocacao.ATIVA;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public int getComputadorId() {
        return computadorId;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public StatusLocacao getStatus() {
        return status;
    }

    // Setters
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setStatus(StatusLocacao status) {
        this.status = status;
    }

    // Métodos úteis
    public long getTempoMinutos() {
        if (fim == null) return 0;
        return Duration.between(inicio, fim).toMinutes();
    }

    public double getTempoHoras() {
        return getTempoMinutos() / 60.0;
    }

    @Override
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
