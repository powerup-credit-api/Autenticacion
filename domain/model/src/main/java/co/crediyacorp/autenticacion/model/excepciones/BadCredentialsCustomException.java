package co.crediyacorp.autenticacion.model.excepciones;

public class BadCredentialsCustomException extends RuntimeException {
    private final String message;

    public BadCredentialsCustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
