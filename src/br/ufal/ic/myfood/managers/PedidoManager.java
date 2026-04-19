package br.ufal.ic.myfood.managers;

import br.ufal.ic.myfood.models.*;
import br.ufal.ic.myfood.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PedidoManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Pedido> pedidos;
    private transient UsuarioManager usuarioManager;
    private transient EmpresaManager empresaManager;
    private transient ProdutoManager produtoManager;

    public PedidoManager(UsuarioManager usuarioManager, EmpresaManager empresaManager, ProdutoManager produtoManager) {
        this.pedidos = new ArrayList<>();
        this.usuarioManager = usuarioManager;
        this.empresaManager = empresaManager;
        this.produtoManager = produtoManager;
    }

    public void setUsuarioManager(UsuarioManager usuarioManager) { this.usuarioManager = usuarioManager; }
    public void setEmpresaManager(EmpresaManager empresaManager) { this.empresaManager = empresaManager; }
    public void setProdutoManager(ProdutoManager produtoManager) { this.produtoManager = produtoManager; }

    public void reset() {
        pedidos.clear();
        Pedido.resetUltimoNumero();
    }

    public int criarPedido(int clienteId, int empresaId)
            throws UsuarioNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        Pessoa cliente = usuarioManager.buscarPorId(clienteId);
        if (cliente == null) throw new UsuarioNaoExisteException();
        if (cliente.isProprietario())
            throw new CampoInvalidoException("Dono de empresa nao pode fazer um pedido");
        Empresa empresa = empresaManager.buscarPorId(empresaId);
        if (empresa == null) throw new EmpresaNaoExisteException();
        for (Pedido p : pedidos) {
            if (p.getClienteId() == clienteId && p.getEmpresaId() == empresaId && p.getEstado().equals("aberto")) {
                throw new CampoInvalidoException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
            }
        }
        Pedido novo = new Pedido(clienteId, empresaId);
        pedidos.add(novo);
        return novo.getNumero();
    }

    public int getNumeroPedido(int clienteId, int empresaId, int indice)
            throws CampoInvalidoException {
        if (indice < 0) throw new CampoInvalidoException("Indice invalido");
        List<Pedido> lista = pedidos.stream()
                .filter(p -> p.getClienteId() == clienteId && p.getEmpresaId() == empresaId)
                .sorted((p1, p2) -> Integer.compare(p1.getNumero(), p2.getNumero()))
                .collect(Collectors.toList());
        if (indice >= lista.size()) throw new CampoInvalidoException("Indice maior que o esperado");
        return lista.get(indice).getNumero();
    }

    public void adicionarProduto(int numeroPedido, int produtoId)
            throws PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException, ProdutoNaoPertenceEmpresaException {
        Pedido p = buscarPorNumero(numeroPedido);
        if (p == null) throw new PedidoNaoEncontradoException("Nao existe pedido em aberto");
        if (!p.getEstado().equals("aberto"))
            throw new PedidoFechadoException("Nao e possivel adcionar produtos a um pedido fechado");
        Produto prod = produtoManager.buscarPorId(produtoId);
        if (prod == null) throw new ProdutoNaoExisteException("Produto nao encontrado");
        if (prod.getEmpresaId() != p.getEmpresaId())
            throw new ProdutoNaoPertenceEmpresaException("O produto nao pertence a essa empresa");
        p.adicionarProduto(prod);
    }

    public void removerProduto(int numeroPedido, String nomeProduto)
            throws CampoInvalidoException, PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException {
        if (nomeProduto == null || nomeProduto.trim().isEmpty())
            throw new CampoInvalidoException("Produto invalido");
        Pedido p = buscarPorNumero(numeroPedido);
        if (p == null) throw new PedidoNaoEncontradoException("Pedido nao encontrado");
        if (!p.getEstado().equals("aberto"))
            throw new PedidoFechadoException("Nao e possivel remover produtos de um pedido fechado");
        boolean removido = p.removerProduto(nomeProduto);
        if (!removido) throw new ProdutoNaoExisteException("Produto nao encontrado");
    }

    public void fecharPedido(int numeroPedido) throws PedidoNaoEncontradoException {
        Pedido p = buscarPorNumero(numeroPedido);
        if (p == null) throw new PedidoNaoEncontradoException("Pedido nao encontrado");
        p.setEstado("preparando");
    }

    public String getPedidos(int numeroPedido, String atributo)
            throws CampoInvalidoException, PedidoNaoEncontradoException {
        if (atributo == null || atributo.trim().isEmpty())
            throw new CampoInvalidoException("Atributo invalido");
        Pedido p = buscarPorNumero(numeroPedido);
        if (p == null) throw new PedidoNaoEncontradoException("Pedido nao encontrado");
        switch (atributo.toLowerCase()) {
            case "cliente":
                Pessoa cliente = usuarioManager.buscarPorId(p.getClienteId());
                return cliente != null ? cliente.getNome() : "";
            case "empresa":
                Empresa empresa = empresaManager.buscarPorId(p.getEmpresaId());
                return empresa != null ? empresa.getNome() : "";
            case "estado":
                return p.getEstado();
            case "produtos":
                return formatarProdutos(p);
            case "valor":
                return String.format(Locale.US, "%.2f", p.getValorTotal());
            default:
                throw new CampoInvalidoException("Atributo nao existe");
        }
    }

    private String formatarProdutos(Pedido p) {
        List<Produto> produtos = p.getProdutos();
        if (produtos.isEmpty()) return "{[]}";
        StringBuilder sb = new StringBuilder("{[");
        for (int i = 0; i < produtos.size(); i++) {
            sb.append(produtos.get(i).getNome());
            if (i < produtos.size() - 1) sb.append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }

    private Pedido buscarPorNumero(int numero) {
        for (Pedido p : pedidos) {
            if (p.getNumero() == numero) return p;
        }
        return null;
    }

    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}