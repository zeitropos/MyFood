package br.ufal.ic.myfood;

import br.ufal.ic.myfood.managers.*;
import br.ufal.ic.myfood.models.*;
import br.ufal.ic.myfood.exceptions.*;
import br.ufal.ic.myfood.persistence.PersistenceHelper;

public class Gerenciador {
    private final UsuarioManager usuarioManager;
    private final EmpresaManager empresaManager;
    private final ProdutoManager produtoManager;
    private final PedidoManager pedidoManager;
    private final PersistenceHelper persistenceHelper;

    public Gerenciador() {
        this.usuarioManager = new UsuarioManager();
        this.empresaManager = new EmpresaManager(usuarioManager);
        this.produtoManager = new ProdutoManager(empresaManager);
        this.pedidoManager = new PedidoManager(usuarioManager, empresaManager, produtoManager);
        this.persistenceHelper = new PersistenceHelper(usuarioManager, empresaManager, produtoManager, pedidoManager);
        this.persistenceHelper.carregarDados();
    }

    public void zerarSistema() { persistenceHelper.zerar(); }

    public void encerrarSistema() { persistenceHelper.salvarDados(); }

    // Delegações para UsuarioManager
    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws CampoInvalidoException, UsuarioJaExisteException {
        usuarioManager.criarCliente(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws CampoInvalidoException, UsuarioJaExisteException {
        usuarioManager.criarProprietario(nome, email, senha, endereco, cpf);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa)
            throws CampoInvalidoException, UsuarioJaExisteException {
        usuarioManager.criarEntregador(nome, email, senha, endereco, veiculo, placa);
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return usuarioManager.getAtributoUsuario(id, atributo);
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        return usuarioManager.login(email, senha);
    }

    // Delegações para EmpresaManager
    // Restaurante
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return empresaManager.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    // Mercado
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco,
                            String abre, String fecha, String tipoMercado)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return empresaManager.criarEmpresa(tipoEmpresa, dono, nome, endereco, abre, fecha, tipoMercado);
    }

    public void alterarFuncionamento(int mercado, String abre, String fecha)
            throws CampoInvalidoException, EmpresaNaoExisteException {
        empresaManager.alterarFuncionamento(mercado, abre, fecha);
    }

    // Farmácia
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco,
                            boolean aberto24Horas, int numeroFuncionarios)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return empresaManager.criarEmpresa(tipoEmpresa, dono, nome, endereco, aberto24Horas, numeroFuncionarios);
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

    // Entregadores
    public void cadastrarEntregador(int empresa, int entregador)
            throws EmpresaNaoExisteException, UsuarioNaoExisteException, CampoInvalidoException {
        empresaManager.cadastrarEntregador(empresa, entregador);
    }

    public String getEntregadores(int empresa)
            throws EmpresaNaoExisteException {
        return empresaManager.getEntregadores(empresa);
    }

    public String getEmpresas(int entregador)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return empresaManager.getEmpresas(entregador);
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

    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, CampoInvalidoException {
        pedidoManager.liberarPedido(numero);
    }
    public int obterPedido(int entregador) throws UsuarioNaoExisteException, EntregadorSemEmpresaException, NaoExistePedidoParaEntregaException, CampoInvalidoException {
        return pedidoManager.obterPedido(entregador);
    }
    public int criarEntrega(int pedido, int entregador, String destino)
            throws PedidoNaoEncontradoException, UsuarioNaoExisteException, PedidoNaoEstaProntoException, EntregadorEmEntregaException, CampoInvalidoException {
        return pedidoManager.criarEntrega(pedido, entregador, destino);
    }
    public String getEntrega(int id, String atributo) throws EntregaNaoEncontradaException, CampoInvalidoException {
        return pedidoManager.getEntrega(id, atributo);
    }
    public int getIdEntrega(int pedido) throws EntregaNaoEncontradaException {
        return pedidoManager.getIdEntrega(pedido);
    }
    public void entregar(int entrega) throws EntregaNaoEncontradaException, PedidoNaoEncontradoException {
        pedidoManager.entregar(entrega);
    }
}