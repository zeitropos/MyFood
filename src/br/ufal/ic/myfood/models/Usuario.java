package br.ufal.ic.myfood.models;

public class Usuario extends Pessoa {
    public Usuario() {}
    public Usuario(String nome, String email, String senha, String endereco) {
        super(nome, email, senha, endereco);
    }
    @Override
    public boolean isProprietario() { return false; }
}