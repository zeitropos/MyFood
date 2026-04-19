package br.ufal.ic.myfood;

import br.ufal.ic.myfood.managers.*;
import br.ufal.ic.myfood.models.*;
import br.ufal.ic.myfood.exceptions.*;
import br.ufal.ic.myfood.persistence.PersistenceManager;
import java.io.File;
import java.util.List;

public class Gerenciador {
    private UsuarioManager usuarioManager;
    private EmpresaManager empresaManager;
    private ProdutoManager produtoManager;
    private PedidoManager pedidoManager;

    public Gerenciador() {
        this.usuarioManager = new UsuarioManager();
        this.empresaManager = new EmpresaManager(usuarioManager);
        this.produtoManager = new ProdutoManager(empresaManager);
        this.pedidoManager = new PedidoManager(usuarioManager, empresaManager, produtoManager);
    }

    public void carregarDados() {
        try {
            if (PersistenceManager.existe()) {
                Object[] dados = PersistenceManager.carregar();
                List<Pessoa> pessoas = (List<Pessoa>) dados[0];
                List<Empresa> empresas = (List<Empresa>) dados[1];
                List<Produto> produtos = (List<Produto>) dados[2];
                List<Pedido> pedidos = (List<Pedido>) dados[3];

                usuarioManager.setPessoas(pessoas);
                empresaManager.setEmpresas(empresas);
                produtoManager.setProdutos(produtos);
                pedidoManager.setPedidos(pedidos);

                // Restaurar contadores
                int maxPessoaId = pessoas.stream().mapToInt(Pessoa::getId).max().orElse(0);
                int maxEmpresaId = empresas.stream().mapToInt(Empresa::getId).max().orElse(0);
                int maxProdutoId = produtos.stream().mapToInt(Produto::getId).max().orElse(0);
                int maxPedidoNum = pedidos.stream().mapToInt(Pedido::getNumero).max().orElse(0);

                Pessoa.setUltimoId(maxPessoaId);
                Empresa.setUltimoId(maxEmpresaId);
                Produto.setUltimoId(maxProdutoId);
                Pedido.setUltimoNumero(maxPedidoNum);

                // Reatribuir referências cruzadas
                empresaManager.setUsuarioManager(usuarioManager);
                produtoManager.setEmpresaManager(empresaManager);
                pedidoManager.setUsuarioManager(usuarioManager);
                pedidoManager.setEmpresaManager(empresaManager);
                pedidoManager.setProdutoManager(produtoManager);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    public void salvarDados() {
        try {
            PersistenceManager.salvar(usuarioManager, empresaManager, produtoManager, pedidoManager);
        } catch (Exception e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public void zerarSistema() {
        usuarioManager.reset();
        empresaManager.reset();
        produtoManager.reset();
        pedidoManager.reset();
        new File("myfood_data.xml").delete();
    }

    public void encerrarSistema() {
        salvarDados();
    }

    // Delegações para UsuarioManager
    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws CampoInvalidoException, UsuarioJaExisteException {
        usuarioManager.criarCliente(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws CampoInvalidoException, UsuarioJaExisteException {
        usuarioManager.criarProprietario(nome, email, senha, endereco, cpf);
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return usuarioManager.getAtributoUsuario(id, atributo);
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        return usuarioManager.login(email, senha);
    }

    // Delegações para EmpresaManager
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return empresaManager.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int idDono) throws UsuarioNaoPodeCriarEmpresaException {
        return empresaManager.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaNaoExisteException {
        return empresaManager.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo)
            throws EmpresaNaoExisteException, CampoInvalidoException {
        return empresaManager.getAtributoEmpresa(empresa, atributo);
    }

    // Delegações para ProdutoManager
    public int criarProduto(int empresa, String nome, double valor, String categoria)
            throws CampoInvalidoException, EmpresaNaoExisteException, ProdutoJaExisteException {
        return produtoManager.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, double valor, String categoria)
            throws CampoInvalidoException, ProdutoNaoExisteException {
        produtoManager.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo)
            throws ProdutoNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        return produtoManager.getProduto(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa) throws EmpresaNaoExisteException {
        return produtoManager.listarProdutos(empresa);
    }

    // Delegações para PedidoManager
    public int criarPedido(int cliente, int empresa)
            throws UsuarioNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        return pedidoManager.criarPedido(cliente, empresa);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws CampoInvalidoException {
        return pedidoManager.getNumeroPedido(cliente, empresa, indice);
    }

    public void adicionarProduto(int numero, int produto)
            throws PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException, ProdutoNaoPertenceEmpresaException {
        pedidoManager.adicionarProduto(numero, produto);
    }

    public void removerProduto(int pedido, String produto)
            throws CampoInvalidoException, PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException {
        pedidoManager.removerProduto(pedido, produto);
    }

    public void fecharPedido(int numero) throws PedidoNaoEncontradoException {
        pedidoManager.fecharPedido(numero);
    }

    public String getPedidos(int numero, String atributo)
            throws CampoInvalidoException, PedidoNaoEncontradoException {
        return pedidoManager.getPedidos(numero, atributo);
    }
}