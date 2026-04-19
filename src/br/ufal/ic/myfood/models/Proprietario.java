package br.ufal.ic.myfood.models;

public class Proprietario extends Pessoa {
    private String cpf;
    public Proprietario() {}
    public Proprietario(String nome, String email, String senha, String endereco, String cpf) {
        super(nome, email, senha, endereco);
        this.cpf = cpf;
    }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    @Override
    public boolean isProprietario() { return true; }
}