package br.ufal.ic.myfood.managers;

import br.ufal.ic.myfood.models.*;
import br.ufal.ic.myfood.exceptions.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;

public class PedidoManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Pedido> pedidos;
    private List<Entrega> entregas;
    private transient UsuarioManager usuarioManager;
    private transient EmpresaManager empresaManager;
    private transient ProdutoManager produtoManager;

    public PedidoManager(UsuarioManager usuarioManager, EmpresaManager empresaManager, ProdutoManager produtoManager) {
        this.pedidos = new ArrayList<>();
        this.entregas = new ArrayList<>();
        this.usuarioManager = usuarioManager;
        this.empresaManager = empresaManager;
        this.produtoManager = produtoManager;
    }

    public void setUsuarioManager(UsuarioManager usuarioManager) {
        this.usuarioManager = usuarioManager;
    }

    public void setEmpresaManager(EmpresaManager empresaManager) {
        this.empresaManager = empresaManager;
    }

    public void setProdutoManager(ProdutoManager produtoManager) {
        this.produtoManager = produtoManager;
    }

    public void reset() {
        pedidos.clear();
        entregas.clear();
        Pedido.resetUltimoNumero();
        Entrega.resetUltimoId();
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
                .sorted(Comparator.comparingInt(Pedido::getNumero))
                .toList();
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

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }

    public void setEntregas(List<Entrega> entregas) {
        this.entregas = entregas;
    }

    public void liberarPedido(int numeroPedido) throws PedidoNaoEncontradoException, CampoInvalidoException {
        Pedido p = buscarPorNumero(numeroPedido);
        if (p == null) throw new PedidoNaoEncontradoException();

        if ("pronto".equals(p.getEstado())) {
            throw new CampoInvalidoException("Pedido ja liberado");
        }
        if (!"preparando".equals(p.getEstado())) {
            throw new CampoInvalidoException("Nao e possivel liberar um produto que nao esta sendo preparado");
        }
        p.setEstado("pronto");
    }

    public int obterPedido(int entregadorId) throws UsuarioNaoExisteException, EntregadorSemEmpresaException, NaoExistePedidoParaEntregaException, CampoInvalidoException {
        Pessoa pessoa = usuarioManager.buscarPorId(entregadorId);
        if (pessoa == null) throw new UsuarioNaoExisteException();
        if (!(pessoa instanceof Entregador entregador))
            throw new CampoInvalidoException("Usuario nao e um entregador");

        List<Integer> empresasIds = entregador.getEmpresasIds();
        if (empresasIds.isEmpty()) throw new EntregadorSemEmpresaException();

        List<Pedido> pedidosProntos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if ("pronto".equals(p.getEstado()) && empresasIds.contains(p.getEmpresaId())) {
                pedidosProntos.add(p);
            }
        }
        if (pedidosProntos.isEmpty()) throw new NaoExistePedidoParaEntregaException();

        List<Pedido> farmacias = new ArrayList<>();
        List<Pedido> outros = new ArrayList<>();
        for (Pedido p : pedidosProntos) {
            Empresa e = empresaManager.buscarPorId(p.getEmpresaId());
            if (e.isFarmacia()) farmacias.add(p);
            else outros.add(p);
        }
        farmacias.sort(Comparator.comparingInt(Pedido::getNumero));
        outros.sort(Comparator.comparingInt(Pedido::getNumero));

        if (!farmacias.isEmpty()) return farmacias.getFirst().getNumero();
        if (!outros.isEmpty()) return outros.getFirst().getNumero();
        throw new NaoExistePedidoParaEntregaException();
    }

    public int criarEntrega(int pedidoNumero, int entregadorId, String destino)
            throws PedidoNaoEncontradoException, UsuarioNaoExisteException, PedidoNaoEstaProntoException, EntregadorEmEntregaException, CampoInvalidoException {

        Pedido pedido = buscarPorNumero(pedidoNumero);
        if (pedido == null) throw new PedidoNaoEncontradoException();
        if (!"pronto".equals(pedido.getEstado()))
            throw new PedidoNaoEstaProntoException();

        Pessoa pessoa = usuarioManager.buscarPorId(entregadorId);
        if (pessoa == null) throw new UsuarioNaoExisteException();
        if (!(pessoa instanceof Entregador entregador))
            throw new CampoInvalidoException("Nao e um entregador valido");

        // Verifica se entregador já está em uma entrega ativa
        for (Entrega e : entregas) {
            Pedido p = buscarPorNumero(e.getPedidoNumero());
            if (p != null && "entregando".equals(p.getEstado()) && e.getEntregadorId() == entregadorId) {
                throw new EntregadorEmEntregaException();
            }
        }

        // Destino = endereço do cliente se não informado
        if (destino == null || destino.trim().isEmpty()) {
            Pessoa cliente = usuarioManager.buscarPorId(pedido.getClienteId());
            if (cliente == null) throw new UsuarioNaoExisteException();
            destino = cliente.getEndereco();
        }

        // Infos da entrega
        String nomeCliente = usuarioManager.buscarPorId(pedido.getClienteId()).getNome();
        Empresa empresa = empresaManager.buscarPorId(pedido.getEmpresaId());
        String nomeEmpresa = empresa.getNome();
        String nomeEntregador = entregador.getNome();
        List<Produto> produtos = new ArrayList<>(pedido.getProdutos());

        Entrega nova = new Entrega(pedidoNumero, entregadorId, destino, nomeCliente, nomeEmpresa, nomeEntregador, produtos);
        entregas.add(nova);
        pedido.setEstado("entregando");
        return nova.getId();
    }

    public String getEntrega(int entregaId, String atributo) throws EntregaNaoEncontradaException, CampoInvalidoException {
        if (atributo == null || atributo.trim().isEmpty())
            throw new CampoInvalidoException("Atributo invalido");
        Entrega e = buscarEntregaPorId(entregaId);
        if (e == null) throw new EntregaNaoEncontradaException();
        return e.getAtributo(atributo);
    }

    public int getIdEntrega(int pedidoNumero) throws EntregaNaoEncontradaException {
        for (Entrega e : entregas) {
            if (e.getPedidoNumero() == pedidoNumero) return e.getId();
        }
        throw new EntregaNaoEncontradaException("Nao existe entrega com esse id");
    }

    public void entregar(int entregaId) throws EntregaNaoEncontradaException, PedidoNaoEncontradoException {
        Entrega e = buscarEntregaPorId(entregaId);
        if (e == null) throw new EntregaNaoEncontradaException("Nao existe nada para ser entregue com esse id");
        Pedido p = buscarPorNumero(e.getPedidoNumero());
        if (p == null) throw new PedidoNaoEncontradoException();
        p.setEstado("entregue");
    }

    private Entrega buscarEntregaPorId(int id) {
        for (Entrega e : entregas) if (e.getId() == id) return e;
        return null;
    }
}