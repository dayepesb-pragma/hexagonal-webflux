package co.pragma.scaffold.infra.driven.r2dbc.adapter;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.out.IUserOutPort;
import co.pragma.scaffold.infra.driven.r2dbc.entity.UserEntity;
import co.pragma.scaffold.infra.driven.r2dbc.helper.ReactiveAdapterOperations;
import co.pragma.scaffold.infra.driven.r2dbc.repository.UserReactiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveUserOutPortAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository> implements IUserOutPort {

    protected ReactiveUserOutPortAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.convertValue(d, User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }
}
