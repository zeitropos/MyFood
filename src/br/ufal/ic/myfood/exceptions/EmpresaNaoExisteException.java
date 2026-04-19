package br.ufal.ic.myfood.exceptions;

public class EmpresaNaoExisteException extends Exception {
    public EmpresaNaoExisteException() {
        super("Empresa nao cadastrada");
    }
    public EmpresaNaoExisteException(String mensagem) {
        super(mensagem);
    }
}