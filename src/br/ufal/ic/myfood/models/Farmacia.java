package br.ufal.ic.myfood.models;

public class Farmacia extends Empresa {
    private boolean aberto24Horas;
    private int numeroFuncionarios;

    public Farmacia() {}

    public Farmacia(String nome, String endereco, int donoId, boolean aberto24Horas, int numeroFuncionarios) {
        super(nome, endereco, donoId);
        this.aberto24Horas = aberto24Horas;
        this.numeroFuncionarios = numeroFuncionarios;
    }

    public boolean isAberto24Horas() { return aberto24Horas; }
    public void setAberto24Horas(boolean aberto24Horas) { this.aberto24Horas = aberto24Horas; }

    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) { this.numeroFuncionarios = numeroFuncionarios; }
}