
package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import it.antonio.sp.entity.UserEntity;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, String> {
    Flux<UserEntity> findByEmail(String email);
}
