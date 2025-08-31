package co.crediyacorp.autenticacion.model.excepciones;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
