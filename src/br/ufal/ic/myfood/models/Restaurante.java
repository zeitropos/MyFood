package br.ufal.ic.myfood.models;

import br.ufal.ic.myfood.exceptions.CampoInvalidoException;

public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante() {}

    public Restaurante(String nome, String endereco, int donoId, String tipoCozinha) {
        super(nome, endereco, donoId);
        this.tipoCozinha = tipoCozinha;
    }
    @Override
    public String getAtributo(String atributo) throws CampoInvalidoException {
        if (atributo.equalsIgnoreCase("tipocozinha")) {
            return tipoCozinha;
        }
        return super.getAtributo(atributo);
    }

    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
}