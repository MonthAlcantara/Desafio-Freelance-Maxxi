package github.io.monthalcantara.desafiomaxxi.exceptions;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(){
        super("Senha Inválida");
    }
}
