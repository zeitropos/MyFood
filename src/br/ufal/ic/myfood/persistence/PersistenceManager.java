package br.ufal.ic.myfood.persistence;

import br.ufal.ic.myfood.managers.*;
import br.ufal.ic.myfood.models.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.List;

public class PersistenceManager {
    private static final String FILE_NAME = "myfood_data.xml";

    public static void salvar(UsuarioManager um, EmpresaManager em, ProdutoManager pm, PedidoManager pedm) throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_NAME)))) {
            encoder.writeObject(um.getPessoas());
            encoder.writeObject(em.getEmpresas());
            encoder.writeObject(pm.getProdutos());
            encoder.writeObject(pedm.getPedidos());
            encoder.writeObject(pedm.getEntregas());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] carregar() throws IOException {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(FILE_NAME)))) {
            List<Pessoa> pessoas = (List<Pessoa>) decoder.readObject();
            List<Empresa> empresas = (List<Empresa>) decoder.readObject();
            List<Produto> produtos = (List<Produto>) decoder.readObject();
            List<Pedido> pedidos = (List<Pedido>) decoder.readObject();
            List<Entrega> entregas = (List<Entrega>) decoder.readObject();
            return new Object[]{pessoas, empresas, produtos, pedidos, entregas};
        }
    }

    public static boolean existe() {
        return new File(FILE_NAME).exists();
    }
}