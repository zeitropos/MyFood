package br.ufal.ic.myfood.exceptions;

public class ProdutoJaExisteException extends Exception {
    public ProdutoJaExisteException(String message) {
        super(message);
    }
}
