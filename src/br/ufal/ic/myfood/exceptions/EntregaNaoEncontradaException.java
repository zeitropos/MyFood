package br.ufal.ic.myfood.exceptions;

public class EntregaNaoEncontradaException extends Exception {
    public EntregaNaoEncontradaException() {
        super("Entrega nao encontrada");
    }
    public EntregaNaoEncontradaException(String message) {
        super(message);
    }
}
