package br.ufal.ic.myfood.managers;

import br.ufal.ic.myfood.models.Empresa;
import br.ufal.ic.myfood.models.Pessoa;
import br.ufal.ic.myfood.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmpresaManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Empresa> empresas;
    private transient UsuarioManager usuarioManager;

    public EmpresaManager(UsuarioManager usuarioManager) {
        this.empresas = new ArrayList<>();
        this.usuarioManager = usuarioManager;
    }

    public void setUsuarioManager(UsuarioManager usuarioManager) {
        this.usuarioManager = usuarioManager;
    }

    public void reset() {
        empresas.clear();
        Empresa.resetUltimoId();
    }

    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco, String tipoCozinha)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        if (!"restaurante".equalsIgnoreCase(tipoEmpresa))
            throw new CampoInvalidoException("Tipo de empresa invalido");
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty())
            throw new CampoInvalidoException("Endereco invalido");
        if (tipoCozinha == null || tipoCozinha.trim().isEmpty())
            throw new CampoInvalidoException("Tipo cozinha invalido");

        for (Empresa e : empresas) {
            if (e.getNome().equalsIgnoreCase(nome) && e.getEndereco().equalsIgnoreCase(endereco)) {
                if (e.getDonoId() == donoId) {
                    throw new EmpresaJaExisteException("Proibido cadastrar duas empresas com o mesmo nome e local");
                } else {
                    throw new EmpresaJaExisteException("Empresa com esse nome ja existe");
                }
            }
        }
        Empresa nova = new Empresa(nome, endereco, tipoCozinha, donoId);
        empresas.add(nova);
        return nova.getId();
    }

    public String getEmpresasDoUsuario(int donoId)
            throws UsuarioNaoPodeCriarEmpresaException {
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        List<Empresa> lista = empresas.stream()
                .filter(e -> e.getDonoId() == donoId)
                .collect(Collectors.toList());
        if (lista.isEmpty()) return "{[]}";
        StringBuilder sb = new StringBuilder("{[[");
        for (int i = 0; i < lista.size(); i++) {
            Empresa e = lista.get(i);
            sb.append(e.getNome()).append(", ").append(e.getEndereco());
            if (i < lista.size() - 1) sb.append("], [");
        }
        sb.append("]]}");
        return sb.toString();
    }

    public int getIdEmpresa(int donoId, String nome, int indice)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaNaoExisteException {
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (indice < 0) throw new CampoInvalidoException("Indice invalido");
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        List<Empresa> lista = empresas.stream()
                .filter(e -> e.getDonoId() == donoId && e.getNome().equalsIgnoreCase(nome))
                .collect(Collectors.toList());
        if (lista.isEmpty()) throw new EmpresaNaoExisteException("Nao existe empresa com esse nome");
        if (indice >= lista.size()) throw new EmpresaNaoExisteException("Indice maior que o esperado");
        return lista.get(indice).getId();
    }

    public String getAtributoEmpresa(int empresaId, String atributo)
            throws EmpresaNaoExisteException, CampoInvalidoException {
        // PRIMEIRO: verifica se a empresa existe
        Empresa e = buscarPorId(empresaId);
        if (e == null) throw new EmpresaNaoExisteException();

        // SEGUNDO: valida o atributo (só se a empresa existir)
        if (atributo == null || atributo.trim().isEmpty()) {
            throw new CampoInvalidoException("Atributo invalido");
        }

        switch (atributo.toLowerCase()) {
            case "nome": return e.getNome();
            case "endereco": return e.getEndereco();
            case "tipocozinha": return e.getTipoCozinha();
            case "dono":
                Pessoa p = usuarioManager.buscarPorId(e.getDonoId());
                return p != null ? p.getNome() : "";
            default: throw new CampoInvalidoException("Atributo invalido");
        }
    }

    public Empresa buscarPorId(int id) {
        for (Empresa e : empresas) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public List<Empresa> getEmpresas() { return empresas; }
    public void setEmpresas(List<Empresa> empresas) { this.empresas = empresas; }
}