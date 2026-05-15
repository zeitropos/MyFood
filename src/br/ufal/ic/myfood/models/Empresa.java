package br.ufal.ic.myfood.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import br.ufal.ic.myfood.exceptions.CampoInvalidoException;

public abstract class Empresa implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static int ultimoId = 0;

    private int id;
    private String nome;
    private String endereco;
    private int donoId;
    private List<Integer> entregadoresIds;

    public Empresa() {
        this.entregadoresIds = new ArrayList<>();
    }

    public Empresa(String nome, String endereco, int donoId) {
        this.id = ++ultimoId;
        this.nome = nome;
        this.endereco = endereco;
        this.donoId = donoId;
        this.entregadoresIds = new ArrayList<>();
    }

    public String getAtributo(String atributo) throws CampoInvalidoException {
        switch (atributo.toLowerCase()) {
            case "nome": return nome;
            case "endereco": return endereco;
            default: throw new CampoInvalidoException("Atributo invalido");
        }
    }

    public boolean isFarmacia() {
        return false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public int getDonoId() { return donoId; }
    public void setDonoId(int donoId) { this.donoId = donoId; }

    public List<Integer> getEntregadoresIds() { return entregadoresIds; }
    public void setEntregadoresIds(List<Integer> ids) { this.entregadoresIds = ids; }
    public void adicionarEntregadorId(int id) { this.entregadoresIds.add(id); }

    public static int getUltimoId() { return ultimoId; }
    public static void setUltimoId(int id) { ultimoId = id; }
    public static void resetUltimoId() { ultimoId = 0; }
}