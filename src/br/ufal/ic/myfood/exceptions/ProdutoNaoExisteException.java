package br.ufal.ic.myfood.exceptions;

public class ProdutoNaoExisteException extends Exception {
    public ProdutoNaoExisteException(String message) {
        super(message);
    }
}
