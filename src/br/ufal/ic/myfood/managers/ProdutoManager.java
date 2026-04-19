package br.ufal.ic.myfood.managers;

import java.util.Locale;
import br.ufal.ic.myfood.models.Produto;
import br.ufal.ic.myfood.models.Empresa;
import br.ufal.ic.myfood.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Produto> produtos;
    private transient EmpresaManager empresaManager;

    public ProdutoManager(EmpresaManager empresaManager) {
        this.produtos = new ArrayList<>();
        this.empresaManager = empresaManager;
    }

    public void setEmpresaManager(EmpresaManager empresaManager) {
        this.empresaManager = empresaManager;
    }

    public void reset() {
        produtos.clear();
        Produto.resetUltimoId();
    }

    public int criarProduto(int empresaId, String nome, double valor, String categoria)
            throws CampoInvalidoException, EmpresaNaoExisteException, ProdutoJaExisteException {
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (valor <= 0) throw new CampoInvalidoException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty())
            throw new CampoInvalidoException("Categoria invalido");

        Empresa e = empresaManager.buscarPorId(empresaId);
        if (e == null) throw new EmpresaNaoExisteException();

        for (Produto p : produtos) {
            if (p.getEmpresaId() == empresaId && p.getNome().equalsIgnoreCase(nome))
                throw new ProdutoJaExisteException("Ja existe um produto com esse nome para essa empresa");
        }
        Produto novo = new Produto(nome, valor, categoria, empresaId);
        produtos.add(novo);
        return novo.getId();
    }

    public void editarProduto(int produtoId, String nome, double valor, String categoria)
            throws CampoInvalidoException, ProdutoNaoExisteException {
        Produto p = buscarPorId(produtoId);
        if (p == null) throw new ProdutoNaoExisteException("Produto nao cadastrado");
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (valor <= 0) throw new CampoInvalidoException("Valor invalido");
        if (categoria == null || categoria.trim().isEmpty())
            throw new CampoInvalidoException("Categoria invalido");
        p.setNome(nome);
        p.setValor(valor);
        p.setCategoria(categoria);
    }

    public String getProduto(String nome, int empresaId, String atributo)
            throws ProdutoNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        Produto p = null;
        for (Produto prod : produtos) {
            if (prod.getEmpresaId() == empresaId && prod.getNome().equalsIgnoreCase(nome)) {
                p = prod;
                break;
            }
        }
        if (p == null) throw new ProdutoNaoExisteException("Produto nao encontrado");
        switch (atributo.toLowerCase()) {
            case "valor": return String.format(Locale.US, "%.2f", p.getValor());
            case "categoria": return p.getCategoria();
            case "empresa":
                Empresa e = empresaManager.buscarPorId(empresaId);
                if (e == null) throw new EmpresaNaoExisteException();
                return e.getNome();
            default: throw new CampoInvalidoException("Atributo nao existe");
        }
    }

    public String listarProdutos(int empresaId) throws EmpresaNaoExisteException {
        Empresa e = empresaManager.buscarPorId(empresaId);
        if (e == null) throw new EmpresaNaoExisteException("Empresa nao encontrada");
        List<Produto> lista = produtos.stream()
                .filter(p -> p.getEmpresaId() == empresaId)
                .collect(Collectors.toList());
        if (lista.isEmpty()) return "{[]}";
        StringBuilder sb = new StringBuilder("{[");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(lista.get(i).getNome());
            if (i < lista.size() - 1) sb.append(", ");
        }
        sb.append("]}");
        return sb.toString();
    }

    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}