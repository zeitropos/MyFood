package br.ufal.ic.myfood.models;

import java.io.Serial;
import java.io.Serializable;

public class Produto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static int ultimoId = 0;

    private int id;
    private String nome;
    private double valor;
    private String categoria;
    private int empresaId;

    public Produto() {}

    public Produto(String nome, double valor, String categoria, int empresaId) {
        this.id = ++ultimoId;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
        this.empresaId = empresaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }

    public static int getUltimoId() { return ultimoId; }
    public static void setUltimoId(int id) { ultimoId = id; }
    public static void resetUltimoId() { ultimoId = 0; }
}