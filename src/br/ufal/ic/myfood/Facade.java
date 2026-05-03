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

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa)
            throws CampoInvalidoException, UsuarioJaExisteException {
        gerenciador.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return gerenciador.getAtributoUsuario(id, atributo);
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        return gerenciador.login(email, senha);
    }

    // Empresas
    // Restaurante
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco, String tipoCozinha)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return gerenciador.criarEmpresa(tipoEmpresa, dono, nome, endereco, tipoCozinha);
    }

    // Mercado
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco,
                            String abre, String fecha, String tipoMercado)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return gerenciador.criarEmpresa(tipoEmpresa, dono, nome, endereco, abre, fecha, tipoMercado);
    }

    public void alterarFuncionamento(int mercado, String abre, String fecha)
            throws CampoInvalidoException, EmpresaNaoExisteException {
        gerenciador.alterarFuncionamento(mercado, abre, fecha);
    }

    // Farmácia
    public int criarEmpresa(String tipoEmpresa, int dono, String nome, String endereco,
                            boolean aberto24Horas, int numeroFuncionarios)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        return gerenciador.criarEmpresa(tipoEmpresa, dono, nome, endereco, aberto24Horas, numeroFuncionarios);
    }

    // Entregadores
    public void cadastrarEntregador(int empresa, int entregador)
            throws EmpresaNaoExisteException, UsuarioNaoExisteException, CampoInvalidoException {
        gerenciador.cadastrarEntregador(empresa, entregador);
    }

    public String getEntregadores(int empresa)
            throws EmpresaNaoExisteException {
        return gerenciador.getEntregadores(empresa);
    }

    public String getEmpresas(int entregador)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        return gerenciador.getEmpresas(entregador);
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

    public void liberarPedido(int numero) throws PedidoNaoEncontradoException, CampoInvalidoException {
        gerenciador.liberarPedido(numero);
    }

    public int obterPedido(int entregador) throws UsuarioNaoExisteException, EntregadorSemEmpresaException, NaoExistePedidoParaEntregaException, CampoInvalidoException {
        return gerenciador.obterPedido(entregador);
    }

    public int criarEntrega(int pedido, int entregador, String destino)
            throws PedidoNaoEncontradoException, UsuarioNaoExisteException, PedidoNaoEstaProntoException, EntregadorEmEntregaException, CampoInvalidoException {
        return gerenciador.criarEntrega(pedido, entregador, destino);
    }

    public String getEntrega(int id, String atributo) throws EntregaNaoEncontradaException, CampoInvalidoException {
        return gerenciador.getEntrega(id, atributo);
    }

    public int getIdEntrega(int pedido) throws EntregaNaoEncontradaException {
        return gerenciador.getIdEntrega(pedido);
    }

    public void entregar(int entrega) throws EntregaNaoEncontradaException, PedidoNaoEncontradoException {
        gerenciador.entregar(entrega);
    }
}