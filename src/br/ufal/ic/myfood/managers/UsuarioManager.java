package br.ufal.ic.myfood.managers;

import br.ufal.ic.myfood.models.Pessoa;
import br.ufal.ic.myfood.models.Usuario;
import br.ufal.ic.myfood.models.Proprietario;
import br.ufal.ic.myfood.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Pessoa> pessoas;

    public UsuarioManager() {
        pessoas = new ArrayList<>();
    }

    public void reset() {
        pessoas.clear();
        Pessoa.resetUltimoId();
    }

    public void criarCliente(String nome, String email, String senha, String endereco)
            throws CampoInvalidoException, UsuarioJaExisteException {
        validarCampos(nome, email, senha, endereco);
        verificarEmailUnico(email);
        pessoas.add(new Usuario(nome, email, senha, endereco));
    }

    public void criarProprietario(String nome, String email, String senha, String endereco, String cpf)
            throws CampoInvalidoException, UsuarioJaExisteException {
        validarCampos(nome, email, senha, endereco);
        if (cpf == null || cpf.trim().isEmpty())
            throw new CampoInvalidoException("CPF invalido");
        if (cpf.length() != 14 || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))
            throw new CampoInvalidoException("CPF invalido");
        verificarEmailUnico(email);
        pessoas.add(new Proprietario(nome, email, senha, endereco, cpf));
    }

    private void validarCampos(String nome, String email, String senha, String endereco)
            throws CampoInvalidoException {
        if (nome == null || nome.trim().isEmpty())
            throw new CampoInvalidoException("Nome invalido");
        if (email == null || email.trim().isEmpty())
            throw new CampoInvalidoException("Email invalido");
        if (!email.contains("@"))
            throw new CampoInvalidoException("Email invalido");
        if (senha == null || senha.trim().isEmpty())
            throw new CampoInvalidoException("Senha invalido");
        if (endereco == null || endereco.trim().isEmpty())
            throw new CampoInvalidoException("Endereco invalido");
    }

    private void verificarEmailUnico(String email) throws UsuarioJaExisteException {
        for (Pessoa p : pessoas) {
            if (p.getEmail().equals(email))
                throw new UsuarioJaExisteException();
        }
    }

    public int login(String email, String senha) throws LoginInvalidoException {
        for (Pessoa p : pessoas) {
            if (p.getEmail().equals(email) && p.getSenha().equals(senha)) {
                return p.getId();
            }
        }
        throw new LoginInvalidoException();
    }

    public String getAtributoUsuario(int id, String atributo)
            throws UsuarioNaoExisteException, CampoInvalidoException {
        Pessoa p = buscarPorId(id);
        if (p == null) throw new UsuarioNaoExisteException();
        switch (atributo.toLowerCase()) {
            case "nome": return p.getNome();
            case "email": return p.getEmail();
            case "senha": return p.getSenha();
            case "endereco": return p.getEndereco();
            case "cpf":
                if (p instanceof Proprietario) return ((Proprietario) p).getCpf();
                else return "";
            default: throw new CampoInvalidoException("Atributo invalido");
        }
    }

    public Pessoa buscarPorId(int id) {
        for (Pessoa p : pessoas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public boolean isProprietario(int id) {
        Pessoa p = buscarPorId(id);
        return p != null && p.isProprietario();
    }

    public List<Pessoa> getPessoas() { return pessoas; }
    public void setPessoas(List<Pessoa> pessoas) { this.pessoas = pessoas; }
}