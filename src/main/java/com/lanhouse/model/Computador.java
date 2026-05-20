package com.lanhouse.model;

/**
 * Representa um computador disponível na Lan House.
 * Contém informações como ID, número da máquina, status, tier e preço por hora.
 * Represents a computer available in the Lan House.
 * Contains information such as ID, machine number, status, tier, and hourly price.
 */
public class Computador {
    private int id;
    private int numero;
    private StatusComputador status;
    private TierComputador tier;
    private double precoHora;

    /**
     * Full constructor to create a Computer object with all attributes, typically used when retrieving data from the database.
     *
     * @param id The unique identifier of the computer.
     * @param numero O número lógico da máquina.
     * @param status O status atual do computador (LIVRE, OCUPADO).
     * @param tier O nível de hardware do computador (BASICO, INTERMEDIARIO, GAMER).
     * @param precoHora O preço cobrado por hora de uso.
     */
    public Computador(int id, int numero, StatusComputador status, TierComputador tier, double precoHora) {
        this.id = id;
        this.numero = numero;
        this.status = status;
        this.tier = tier;
        this.precoHora = precoHora;
    }

    /**
     * Constructor to create a new Computer without an ID, typically used before persisting to the database.
     *
     * @param numero The logical machine number.
     * @param status O status inicial do computador (LIVRE, OCUPADO).
     * @param tier O nível de hardware do computador (BASICO, INTERMEDIARIO, GAMER).
     * @param precoHora O preço cobrado por hora de uso.
     */
    public Computador(int numero, StatusComputador status, TierComputador tier, double precoHora) {
        this.numero = numero;
        this.status = status;
        this.tier = tier;
        this.precoHora = precoHora;
    }

    // Getters
    /**
     * @return The computer's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The machine number.
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @return The {@link StatusComputador} of the computer.
     */
    public StatusComputador getStatus() {
        return status;
    }

    /**
     * @return The {@link TierComputador} of the computer.
     */
    public TierComputador getTier() {
        return tier;
    }

    /**
     * @return The hourly price.
     */
    public double getPrecoHora() {
        return precoHora;
    }

    // Setters
    /**
     * Sets the computer's ID. This method is typically used by DAOs after persistence to assign the database-generated ID.
     * @param id The new computer ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the logical machine number.
     * @param numero The new machine number.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * Sets the current status of the computer.
     * @param status The new {@link StatusComputador}.
     */
    public void setStatus(StatusComputador status) {
        this.status = status;
    }

    /**
     * Sets the tier (level) of the computer.
     * @param tier The new {@link TierComputador}.
     */
    public void setTier(TierComputador tier) {
        this.tier = tier;
    }

    /**
     * Sets the hourly price charged for using the computer.
     * @param precoHora The new hourly price.
     */
    public void setPrecoHora(double precoHora) {
        this.precoHora = precoHora;
    }

    @Override
    /**
     */
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
