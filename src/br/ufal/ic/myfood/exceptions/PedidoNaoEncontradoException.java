package br.ufal.ic.myfood.exceptions;

public class PedidoNaoEncontradoException extends Exception {
    public PedidoNaoEncontradoException() {
        super("Pedido nao encontrado");
    }
    public PedidoNaoEncontradoException(String message) {
        super(message);
    }
}