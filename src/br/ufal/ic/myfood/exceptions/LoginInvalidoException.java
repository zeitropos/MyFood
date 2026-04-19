package br.ufal.ic.myfood.exceptions;

public class LoginInvalidoException extends Exception {
    public LoginInvalidoException() {
        super("Login ou senha invalidos");
    }
}
