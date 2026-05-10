package com.lanhouse.model;

public class Computador {
    private int id;
    private int numero;
    private String status;
    private String tier;
    private double precoHora;

    public Computador(int id, int numero, String status, String tier, double precoHora) {
        this.id = id;
        this.numero = numero;
        this.status = status;
        this.tier = tier;
        this.precoHora = precoHora;
    }

    public Computador(int numero, String status, String tier, double precoHora) {
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

    public String getStatus() {
        return status;
    }

    public String getTier() {
        return tier;
    }

    public double getPrecoHora() {
        return precoHora;
    }

    // Setters
    public void setStatus(String status) {
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
                ", status='" + status + '\'' +
                ", tier='" + tier + '\'' +
                ", precoHora=" + precoHora +
                '}';
    }
}
