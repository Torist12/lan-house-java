package com.lanhouse.model;

public class Funcionario {
    private int id;
    private String usuario;
    private String senha;

    public Funcionario(int id, String usuario, String senha) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
    }

    public Funcionario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
