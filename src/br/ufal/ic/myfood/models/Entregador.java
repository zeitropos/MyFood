package br.ufal.ic.myfood.models;

import br.ufal.ic.myfood.exceptions.CampoInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class Entregador extends Pessoa {
    private String veiculo;
    private String placa;
    private List<Integer> empresasIds; // IDs das empresas onde trabalha

    public Entregador() {
        this.empresasIds = new ArrayList<>();
    }

    public Entregador(String nome, String email, String senha, String endereco, String veiculo, String placa) {
        super(nome, email, senha, endereco);
        this.veiculo = veiculo;
        this.placa = placa;
        this.empresasIds = new ArrayList<>();
    }

    public String getVeiculo() { return veiculo; }
    public void setVeiculo(String veiculo) { this.veiculo = veiculo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public List<Integer> getEmpresasIds() { return empresasIds; }
    public void setEmpresasIds(List<Integer> empresasIds) { this.empresasIds = empresasIds; }
    public void adicionarEmpresaId(int empresaId) { this.empresasIds.add(empresaId); }

    @Override
    public String getAtributo(String atributo) throws CampoInvalidoException {
        switch (atributo.toLowerCase()) {
            case "veiculo": return veiculo;
            case "placa": return placa;
            default: return super.getAtributo(atributo);
        }
    }
}