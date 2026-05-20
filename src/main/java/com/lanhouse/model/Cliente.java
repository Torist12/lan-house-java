package com.lanhouse.model;

/**
 * Represents a client of the Lan House.
 * Contains basic information such as ID, name, document, and phone number.
 */
public class Cliente {
    private int id;
    private String nome;
    private String documento;
    private String telefone;

    /**
     * Full constructor to create a Client object with all attributes,
     * typically used when retrieving data from the database.
     * @param id The unique identifier of the client.
     * @param nome O nome completo do cliente.
     * @param documento O número do documento (CPF/CNPJ) do cliente.
     * @param telefone O número de telefone para contato do cliente.
     */
    public Cliente(int id, String nome, String documento, String telefone) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
    }

    /**
     * Constructor to create a new Client without an ID,
     * typically used before persisting to the database.
     * @param documento O número do documento (CPF/CNPJ) do cliente.
     * @param telefone O número de telefone para contato do cliente.
     */
    public Cliente(String nome, String documento, String telefone) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
    }

    // Getters
    public int getId() {
        /**
         * @return The client's ID.
         */
        return id;
    }

    public String getNome() {
        /**
         * @return The client's name.
         */
        return nome;
    }

    public String getDocumento() {
        /**
         * @return The client's document.
         */
        return documento;
    }

    public String getTelefone() {
        /**
         * @return The client's phone number.
         */
        return telefone;
    }

    // Setters
    /**
     * Sets the client's ID. This method is typically used by DAOs
     * after persistence to assign the database-generated ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the client's name.
     * @param nome The new name of the client.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Sets the client's phone number.
     * @param telefone The new phone number of the client.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    /**
     */
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento='" + documento + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
