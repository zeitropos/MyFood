package br.ufal.ic.myfood.exceptions;

public class PedidoNaoEstaProntoException extends Exception {
    public PedidoNaoEstaProntoException() {
        super("Pedido nao esta pronto para entrega");
    }
}