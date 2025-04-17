package co.pragma.scaffold.infra.driven.r2dbc.adapter;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.out.IUserRepository;
import co.pragma.scaffold.infra.driven.r2dbc.entity.UserEntity;
import co.pragma.scaffold.infra.driven.r2dbc.helper.ReactiveAdapterOperations;
import co.pragma.scaffold.infra.driven.r2dbc.repository.UserReactiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ReactiveUserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserReactiveRepository> implements IUserRepository {

    protected ReactiveUserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.convertValue(d, User.class));
    }
}
