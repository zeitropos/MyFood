package br.ufal.ic.myfood.models;

import java.io.Serializable;

public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int ultimoId = 0;

    private int id;
    private String nome;
    private String endereco;
    private String tipoCozinha;
    private int donoId;

    public Empresa() {}

    public Empresa(String nome, String endereco, String tipoCozinha, int donoId) {
        this.id = ++ultimoId;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.donoId = donoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }

    public int getDonoId() { return donoId; }
    public void setDonoId(int donoId) { this.donoId = donoId; }

    public static int getUltimoId() { return ultimoId; }
    public static void setUltimoId(int id) { ultimoId = id; }
    public static void resetUltimoId() { ultimoId = 0; }
}