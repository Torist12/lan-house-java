package com.lanhouse.model;

public class Computador {
    private int id;
    private int numero;
    private StatusComputador status;
    private TierComputador tier;
    private double precoHora;

    public Computador(int id, int numero, StatusComputador status, TierComputador tier, double precoHora) {
        this.id = id;
        this.numero = numero;
        this.status = status;
        this.tier = tier;
        this.precoHora = precoHora;
    }

    public Computador(int numero, StatusComputador status, TierComputador tier, double precoHora) {
        this.numero = numero;
        this.status = status;
        this.tier = tier;
        this.precoHora = precoHora;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public StatusComputador getStatus() {
        return status;
    }

    public TierComputador getTier() {
        return tier;
    }

    public double getPrecoHora() {
        return precoHora;
    }

    // Setters
    public void setStatus(StatusComputador status) {
        this.status = status;
    }

    public void setPrecoHora(double precoHora) {
        this.precoHora = precoHora;
    }

    @Override
    public String toString() {
        return "Computador{" +
                "id=" + id +
                ", numero=" + numero +
                ", status=" + status +
                ", tier=" + tier +
                ", precoHora=" + precoHora +
                '}';
    }
}
