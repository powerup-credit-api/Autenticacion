package co.crediyacorp.autenticacion.config;

import co.crediyacorp.autenticacion.model.autenticacion.TokenAutenticacionPort;
import co.crediyacorp.autenticacion.model.transactions.TransactionalWrapper;
import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioUseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'UseCase' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public TransactionalWrapper transactionalWrapper() {
            return new TransactionalWrapper() {
                @Override
                public <T> Mono<T> executeInTransaction(Mono<T> action) {
                    return action;
                }
            };
        }

        @Bean
        public UsuarioRepository usuarioRepository() {
            return new UsuarioRepository() {
                @Override
                public Mono<Usuario> guardarUsuario(Usuario usuario) {
                    return Mono.just(usuario);
                }

                @Override
                public Mono<Boolean> existeUsuarioPorEmail(String email) {
                    return Mono.just(false);
                }

                @Override
                public Mono<Boolean> existeUsuarioPorDocumentoIdentidad(String documento) {
                    return Mono.just(false);
                }

                @Override
                public Mono<Boolean> existeUsuarioPorEmailYDocumentoIdentidad(String documento, String email) {
                    return Mono.just(false);
                }

                @Override
                public Mono<BigDecimal> obtenerSalarioBasePorEmail(String email) {
                    return Mono.just(new BigDecimal("1000000"));
                }

                @Override
                public Mono<Usuario> buscarUsuarioPorEmail(String email) {
                    return Mono.empty();
                }
            };
        }

        @Bean
        public TokenAutenticacionPort tokenAutenticacionPort() {
            return (email, contrasena) -> Mono.just("fake-token");
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }
}
