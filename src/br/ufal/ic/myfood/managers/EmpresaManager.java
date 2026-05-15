package br.ufal.ic.myfood.managers;

import br.ufal.ic.myfood.models.*;
import br.ufal.ic.myfood.exceptions.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmpresaManager implements Serializable {
    @Serial
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

    // Validações
    private void validarHorarios(String abre, String fecha) throws CampoInvalidoException {
        if (abre == null || fecha == null) {
            throw new CampoInvalidoException("Horario invalido");
        }

        if (abre.trim().isEmpty() || fecha.trim().isEmpty()) {
            throw new CampoInvalidoException("Formato de hora invalido");
        }

        if (abre.length() != 5 || abre.charAt(2) != ':' ||
                fecha.length() != 5 || fecha.charAt(2) != ':') {
            throw new CampoInvalidoException("Formato de hora invalido");
        }

        int abreH, abreM, fechaH, fechaM;
        try {
            abreH = Integer.parseInt(abre.substring(0, 2));
            abreM = Integer.parseInt(abre.substring(3, 5));
            fechaH = Integer.parseInt(fecha.substring(0, 2));
            fechaM = Integer.parseInt(fecha.substring(3, 5));
        } catch (NumberFormatException e) {
            throw new CampoInvalidoException("Formato de hora invalido");
        }

        if (abreH < 0 || abreH > 23 || abreM < 0 || abreM > 59 ||
                fechaH < 0 || fechaH > 23 || fechaM < 0 || fechaM > 59) {
            throw new CampoInvalidoException("Horario invalido");
        }

        if (fechaH < abreH || (fechaH == abreH && fechaM <= abreM)) {
            throw new CampoInvalidoException("Horario invalido");
        }
    }

    private void verificarDuplicidade(String nome, String endereco, int donoId) throws EmpresaJaExisteException {
        for (Empresa e : empresas) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                if (e.getDonoId() != donoId) {
                    throw new EmpresaJaExisteException("Empresa com esse nome ja existe");
                } else if (e.getEndereco().equalsIgnoreCase(endereco)) {
                    throw new EmpresaJaExisteException("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
            }
        }
    }

    // Restaurante
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

        verificarDuplicidade(nome, endereco, donoId);

        Restaurante novo = new Restaurante(nome, endereco, donoId, tipoCozinha);
        empresas.add(novo);
        return novo.getId();
    }

    // Mercado
    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco,
                            String abre, String fecha, String tipoMercado)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        if (!"mercado".equalsIgnoreCase(tipoEmpresa))
            throw new CampoInvalidoException("Tipo de empresa invalido");
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty())
            throw new CampoInvalidoException("Endereco da empresa invalido");
        if (tipoMercado == null || tipoMercado.trim().isEmpty())
            throw new CampoInvalidoException("Tipo de mercado invalido");

        validarHorarios(abre, fecha);

        verificarDuplicidade(nome, endereco, donoId);

        Mercado novo = new Mercado(nome, endereco, donoId, abre, fecha, tipoMercado);
        empresas.add(novo);
        return novo.getId();
    }

    public void alterarFuncionamento(int empresaId, String abre, String fecha)
            throws CampoInvalidoException, EmpresaNaoExisteException {
        Empresa e = buscarPorId(empresaId);
        if (e == null)
            throw new EmpresaNaoExisteException("Nao e um mercado valido");
        if (!(e instanceof Mercado m))
            throw new CampoInvalidoException("Nao e um mercado valido");

        validarHorarios(abre, fecha);

        m.setAbre(abre);
        m.setFecha(fecha);
    }

    // Farmácia
    public int criarEmpresa(String tipoEmpresa, int donoId, String nome, String endereco,
                            boolean aberto24Horas, int numeroFuncionarios)
            throws CampoInvalidoException, UsuarioNaoPodeCriarEmpresaException, EmpresaJaExisteException {
        if (!"farmacia".equalsIgnoreCase(tipoEmpresa))
            throw new CampoInvalidoException("Tipo de empresa invalido");
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (endereco == null || endereco.trim().isEmpty())
            throw new CampoInvalidoException("Endereco da empresa invalido");
        if (numeroFuncionarios <= 0)
            throw new CampoInvalidoException("Numero de funcionarios invalido");

        verificarDuplicidade(nome, endereco, donoId);

        Farmacia novo = new Farmacia(nome, endereco, donoId, aberto24Horas, numeroFuncionarios);
        empresas.add(novo);
        return novo.getId();
    }

    // Entregadores
    public void cadastrarEntregador(int empresaId, int entregadorId)
            throws EmpresaNaoExisteException, UsuarioNaoExisteException, CampoInvalidoException {
        Empresa empresa = buscarPorId(empresaId);
        if (empresa == null) throw new EmpresaNaoExisteException();

        Pessoa pessoa = usuarioManager.buscarPorId(entregadorId);
        if (pessoa == null) throw new UsuarioNaoExisteException();
        if (!(pessoa instanceof Entregador entregador))
            throw new CampoInvalidoException("Usuario nao e um entregador");

        if (empresa.getEntregadoresIds().contains(entregadorId))
            throw new CampoInvalidoException("Entregador ja cadastrado");

        empresa.adicionarEntregadorId(entregadorId);
        entregador.adicionarEmpresaId(empresaId);
    }

    public String getEntregadores(int empresaId) throws EmpresaNaoExisteException {
        Empresa empresa = buscarPorId(empresaId);
        if (empresa == null) throw new EmpresaNaoExisteException();

        List<String> emails = new ArrayList<>();
        for (int id : empresa.getEntregadoresIds()) {
            Pessoa p = usuarioManager.buscarPorId(id);
            if (p instanceof Entregador) {
                emails.add(p.getEmail());
            }
        }
        return "{[" + String.join(", ", emails) + "]}";
    }

    public String getEmpresas(int entregadorId) throws UsuarioNaoExisteException, CampoInvalidoException {
        Pessoa p = usuarioManager.buscarPorId(entregadorId);
        if (p == null) throw new UsuarioNaoExisteException();
        if (!(p instanceof Entregador entregador))
            throw new CampoInvalidoException("Usuario nao e um entregador");

        List<String> empresasInfo = new ArrayList<>();
        for (int idEmp : entregador.getEmpresasIds()) {
            Empresa e = buscarPorId(idEmp);
            if (e != null) {
                empresasInfo.add("[" + e.getNome() + ", " + e.getEndereco() + "]");
            }
        }
        return "{[" + String.join(", ", empresasInfo) + "]}";
    }

    // Consultas
    public String getEmpresasDoUsuario(int donoId) throws UsuarioNaoPodeCriarEmpresaException {
        if (!usuarioManager.isProprietario(donoId))
            throw new UsuarioNaoPodeCriarEmpresaException();
        List<Empresa> lista = empresas.stream()
                .filter(e -> e.getDonoId() == donoId)
                .toList();
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
                .toList();
        if (lista.isEmpty()) throw new EmpresaNaoExisteException("Nao existe empresa com esse nome");
        if (indice >= lista.size()) throw new EmpresaNaoExisteException("Indice maior que o esperado");
        return lista.get(indice).getId();
    }

    public String getAtributoEmpresa(int empresaId, String atributo)
            throws EmpresaNaoExisteException, CampoInvalidoException {
        Empresa e = buscarPorId(empresaId);
        if (e == null) throw new EmpresaNaoExisteException();

        if (atributo == null || atributo.trim().isEmpty())
            throw new CampoInvalidoException("Atributo invalido");

        if (atributo.equalsIgnoreCase("dono")) {
            Pessoa dono = usuarioManager.buscarPorId(e.getDonoId());
            return dono != null ? dono.getNome() : "";
        }
        return e.getAtributo(atributo);
    }

    public Empresa buscarPorId(int id) {
        return empresas.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public List<Empresa> getEmpresas() { return empresas; }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;

        int maxId = empresas.stream().mapToInt(Empresa::getId).max().orElse(0);
        Empresa.setUltimoId(maxId);
    }
}