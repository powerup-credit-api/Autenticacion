package co.crediyacorp.autenticacion.api.config.manejoexcepciones;

import co.crediyacorp.autenticacion.model.excepciones.BadCredentialsCustomException;
import co.crediyacorp.autenticacion.model.excepciones.UsernameNotFoundException;
import co.crediyacorp.autenticacion.model.excepciones.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAtributes extends DefaultErrorAttributes
{
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> atributes = super.getErrorAttributes(request, options);
        Throwable error = getError(request);

        HttpStatus status;
        String message = error.getMessage();

        if (error instanceof ValidationException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (error instanceof BadCredentialsCustomException || error instanceof UsernameNotFoundException) {
            status = HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Ocurrió un error inesperado";
        }

        atributes.put("status", status.value());
        atributes.put("error", status.getReasonPhrase());
        atributes.put("message", message);

        return atributes;
    }

}
