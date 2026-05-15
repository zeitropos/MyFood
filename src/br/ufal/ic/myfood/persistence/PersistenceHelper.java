package br.ufal.ic.myfood.persistence;

import br.ufal.ic.myfood.managers.*;
import br.ufal.ic.myfood.models.*;
import java.io.File;
import java.util.List;

public class PersistenceHelper {
    private final UsuarioManager usuarioManager;
    private final EmpresaManager empresaManager;
    private final ProdutoManager produtoManager;
    private final PedidoManager pedidoManager;

    public PersistenceHelper(UsuarioManager um, EmpresaManager em, ProdutoManager pm, PedidoManager pedm) {
        this.usuarioManager = um;
        this.empresaManager = em;
        this.produtoManager = pm;
        this.pedidoManager = pedm;
    }

    public void carregarDados() {
        try {
            if (PersistenceManager.existe()) {
                Object[] dados = PersistenceManager.carregar();
                List<Pessoa> pessoas = (List<Pessoa>) dados[0];
                List<Empresa> empresas = (List<Empresa>) dados[1];
                List<Produto> produtos = (List<Produto>) dados[2];
                List<Pedido> pedidos = (List<Pedido>) dados[3];
                List<Entrega> entregas = (List<Entrega>) dados[4];

                usuarioManager.setPessoas(pessoas);
                empresaManager.setEmpresas(empresas);
                produtoManager.setProdutos(produtos);
                pedidoManager.setPedidos(pedidos);
                pedidoManager.setEntregas(entregas);

                // Restaurar contadores
                int maxPessoaId = pessoas.stream().mapToInt(Pessoa::getId).max().orElse(0);
                int maxEmpresaId = empresas.stream().mapToInt(Empresa::getId).max().orElse(0);
                int maxProdutoId = produtos.stream().mapToInt(Produto::getId).max().orElse(0);
                int maxPedidoNum = pedidos.stream().mapToInt(Pedido::getNumero).max().orElse(0);
                int maxEntregaId = entregas.stream().mapToInt(Entrega::getId).max().orElse(0);

                Pessoa.setUltimoId(maxPessoaId);
                Empresa.setUltimoId(maxEmpresaId);
                Produto.setUltimoId(maxProdutoId);
                Pedido.setUltimoNumero(maxPedidoNum);
                Entrega.setUltimoId(maxEntregaId);

                // Reatribuir dependências (referências circulares)
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

    public void zerar() {
        usuarioManager.reset();
        empresaManager.reset();
        produtoManager.reset();
        pedidoManager.reset();
        new File("myfood_data.xml").delete();
    }
}