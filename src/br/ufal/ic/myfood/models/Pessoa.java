package br.ufal.ic.myfood.models;

import java.io.Serializable;

public abstract class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int ultimoId = 0;

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String endereco;

    public Pessoa() {}

    public Pessoa(String nome, String email, String senha, String endereco) {
        this.id = ++ultimoId;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public static int getUltimoId() { return ultimoId; }
    public static void setUltimoId(int id) { ultimoId = id; }
    public static void resetUltimoId() { ultimoId = 0; }

    public boolean isProprietario() { return false; }
}