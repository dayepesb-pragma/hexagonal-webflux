package co.pragma.scaffold.infra.driven.r2dbc.repository;

import co.pragma.scaffold.infra.driven.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository  extends ReactiveCrudRepository<UserEntity , Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<UserEntity> findByEmail(String email);
}
