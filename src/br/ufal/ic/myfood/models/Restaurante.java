package br.ufal.ic.myfood.models;

public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante() {}

    public Restaurante(String nome, String endereco, int donoId, String tipoCozinha) {
        super(nome, endereco, donoId);
        this.tipoCozinha = tipoCozinha;
    }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
}