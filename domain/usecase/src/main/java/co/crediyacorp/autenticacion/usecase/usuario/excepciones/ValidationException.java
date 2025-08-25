package co.crediyacorp.autenticacion.usecase.usuario.excepciones;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
