package br.ufal.ic.myfood.models;

import br.ufal.ic.myfood.exceptions.CampoInvalidoException;

public class Farmacia extends Empresa {
    private boolean aberto24Horas;
    private int numeroFuncionarios;

    public Farmacia() {}

    public Farmacia(String nome, String endereco, int donoId, boolean aberto24Horas, int numeroFuncionarios) {
        super(nome, endereco, donoId);
        this.aberto24Horas = aberto24Horas;
        this.numeroFuncionarios = numeroFuncionarios;
    }
    @Override
    public String getAtributo(String atributo) throws CampoInvalidoException {
        switch (atributo.toLowerCase()) {
            case "aberto24horas": return String.valueOf(aberto24Horas);
            case "numerofuncionarios": return String.valueOf(numeroFuncionarios);
            default: return super.getAtributo(atributo);
        }
    }

    @Override
    public boolean isFarmacia() {
        return true;
    }

    public boolean isAberto24Horas() { return aberto24Horas; }
    public void setAberto24Horas(boolean aberto24Horas) { this.aberto24Horas = aberto24Horas; }

    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) { this.numeroFuncionarios = numeroFuncionarios; }
}