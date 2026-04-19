package br.ufal.ic.myfood;

import br.ufal.ic.myfood.exceptions.*;

public class Facade {
    private Gerenciador gerenciador;

    public Facade() {
        gerenciador = new Gerenciador();
        gerenciador.carregarDados();
    }

    public void zerarSistema() {
        gerenciador.zerarSistema();
    }

    public void encerrarSistema() {
        gerenciador.encerrarSistema();
    }

    // Usuários
    public void criarUsuario(String nome, String email, String senha, String endereco)
            throws CampoInvalidoException, UsuarioJaExisteException {
        gerenciador.criarUsuario(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf)
            throws CampoInvalidoException, UsuarioJaExisteException {
        gerenciador.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return gerenciador.getAtributoUsuario(id, atributo);
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        return gerenciador.login(email, senha);
    }

    // Empresas
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return gerenciador.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int idDono) throws UsuarioNaoPodeCriarEmpresaException {
        return gerenciador.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaNaoExisteException {
        return gerenciador.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresa, String atributo)
            throws EmpresaNaoExisteException, CampoInvalidoException {
        return gerenciador.getAtributoEmpresa(empresa, atributo);
    }

    // Produtos
    public int criarProduto(int empresa, String nome, double valor, String categoria)
            throws CampoInvalidoException, EmpresaNaoExisteException, ProdutoJaExisteException {
        return gerenciador.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, double valor, String categoria)
            throws CampoInvalidoException, ProdutoNaoExisteException {
        gerenciador.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo)
            throws ProdutoNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        return gerenciador.getProduto(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa) throws EmpresaNaoExisteException {
        return gerenciador.listarProdutos(empresa);
    }

    // Pedidos
    public int criarPedido(int cliente, int empresa)
            throws UsuarioNaoExisteException, CampoInvalidoException, EmpresaNaoExisteException {
        return gerenciador.criarPedido(cliente, empresa);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws CampoInvalidoException {
        return gerenciador.getNumeroPedido(cliente, empresa, indice);
    }

    public void adicionarProduto(int numero, int produto)
            throws PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException, ProdutoNaoPertenceEmpresaException {
        gerenciador.adicionarProduto(numero, produto);
    }

    public void removerProduto(int pedido, String produto)
            throws CampoInvalidoException, PedidoNaoEncontradoException, PedidoFechadoException, ProdutoNaoExisteException {
        gerenciador.removerProduto(pedido, produto);
    }

    public void fecharPedido(int numero) throws PedidoNaoEncontradoException {
        gerenciador.fecharPedido(numero);
    }

    public String getPedidos(int numero, String atributo)
            throws CampoInvalidoException, PedidoNaoEncontradoException {
        return gerenciador.getPedidos(numero, atributo);
    }
}