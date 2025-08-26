package co.crediyacorp.autenticacion.usecase.usuario.transaction_usecase;

import co.crediyacorp.autenticacion.model.transactions.TransactionalWrapper;
import co.crediyacorp.autenticacion.model.usuario.Usuario;
import co.crediyacorp.autenticacion.usecase.usuario.usecases.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ExecuteUsuarioUseCase {

    private final TransactionalWrapper transactionalWrapper;
    private final UsuarioUseCase usuarioUseCase;

    public Mono<Usuario> executeGuardarUsuario(Usuario usuario) {
        return transactionalWrapper.executeInTransaction(usuarioUseCase.crearUsuario(usuario));
    }

}
