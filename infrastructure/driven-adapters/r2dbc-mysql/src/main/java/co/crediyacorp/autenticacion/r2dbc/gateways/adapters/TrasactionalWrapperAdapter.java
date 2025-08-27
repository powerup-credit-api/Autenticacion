package co.crediyacorp.autenticacion.r2dbc.gateways.adapters;
import co.crediyacorp.autenticacion.model.transactions.TransactionalWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrasactionalWrapperAdapter implements TransactionalWrapper {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> executeInTransaction(Mono<T> action) {
        return action.as(transactionalOperator::transactional);
    }
}
