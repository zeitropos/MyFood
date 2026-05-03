package br.ufal.ic.myfood.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.ufal.ic.myfood.exceptions.CampoInvalidoException;

public class Entrega implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int ultimoId = 0;

    private int id;
    private int pedidoNumero;
    private int entregadorId;
    private String destino;

    private String nomeCliente;
    private String nomeEmpresa;
    private String nomeEntregador;
    private List<Produto> produtos = new ArrayList<>();

    public Entrega() {}

    public Entrega(int pedidoNumero, int entregadorId, String destino,
                   String nomeCliente, String nomeEmpresa, String nomeEntregador,
                   List<Produto> produtos) {
        this.id = ++ultimoId;
        this.pedidoNumero = pedidoNumero;
        this.entregadorId = entregadorId;
        this.destino = destino;
        this.nomeCliente = nomeCliente;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeEntregador = nomeEntregador;
        this.produtos = new ArrayList<>(produtos);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPedidoNumero() { return pedidoNumero; }
    public void setPedidoNumero(int pedidoNumero) { this.pedidoNumero = pedidoNumero; }

    public int getEntregadorId() { return entregadorId; }
    public void setEntregadorId(int entregadorId) { this.entregadorId = entregadorId; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public String getNomeEntregador() { return nomeEntregador; }
    public void setNomeEntregador(String nomeEntregador) { this.nomeEntregador = nomeEntregador; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

    public String getAtributo(String atributo) throws CampoInvalidoException {
        if (atributo == null || atributo.trim().isEmpty()) throw new CampoInvalidoException("Atributo invalido");
        switch (atributo.toLowerCase()) {
            case "cliente": return nomeCliente;
            case "empresa": return nomeEmpresa;
            case "pedido": return String.valueOf(pedidoNumero);
            case "entregador": return nomeEntregador;
            case "destino": return destino;
            case "produtos":
                if (produtos.isEmpty()) return "{[]}";
                StringBuilder sb = new StringBuilder("{[");
                for (int i = 0; i < produtos.size(); i++) {
                    sb.append(produtos.get(i).getNome());
                    if (i < produtos.size() - 1) sb.append(", ");
                }
                sb.append("]}");
                return sb.toString();
            default: throw new CampoInvalidoException("Atributo nao existe");
        }
    }

    public static int getUltimoId() { return ultimoId; }
    public static void setUltimoId(int id) { ultimoId = id; }
    public static void resetUltimoId() { ultimoId = 0; }
}