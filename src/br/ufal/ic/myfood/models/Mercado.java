package br.ufal.ic.myfood.models;

import br.ufal.ic.myfood.exceptions.CampoInvalidoException;

public class Mercado extends Empresa {
    private String abre;
    private String fecha;
    private String tipoMercado;

    public Mercado() {}

    public Mercado(String nome, String endereco, int donoId, String abre, String fecha, String tipoMercado) {
        super(nome, endereco, donoId);
        this.abre = abre;
        this.fecha = fecha;
        this.tipoMercado = tipoMercado;
    }
    @Override
    public String getAtributo(String atributo) throws CampoInvalidoException {
        switch (atributo.toLowerCase()) {
            case "abre": return abre;
            case "fecha": return fecha;
            case "tipomercado": return tipoMercado;
            default: return super.getAtributo(atributo);
        }
    }

    public String getAbre() { return abre; }
    public void setAbre(String abre) { this.abre = abre; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipoMercado() { return tipoMercado; }
    public void setTipoMercado(String tipoMercado) { this.tipoMercado = tipoMercado; }
}