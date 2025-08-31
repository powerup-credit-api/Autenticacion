package co.crediyacorp.autenticacion.seguridad.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import reactor.core.publisher.Mono;

import java.util.Date;

public class JwtProvider {

    private final String secret;
    private final long expiration;

    public JwtProvider(String secret, long expiration) {
        this.secret = secret;
        this.expiration = expiration;
    }

    public Mono<String> generateToken(String email, String role) {
        return Mono.fromSupplier(() -> {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + expiration);

            return JWT.create()
                    .withSubject(email)
                    .withClaim("role", role)
                    .withIssuedAt(now)
                    .withExpiresAt(expiry)
                    .sign(Algorithm.HMAC256(secret));
        });
    }
}