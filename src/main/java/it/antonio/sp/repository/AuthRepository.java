package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import it.antonio.sp.entity.AuthEntity;
import reactor.core.publisher.Flux;

@Repository
public interface AuthRepository extends ReactiveCrudRepository<AuthEntity, String> {
    Flux<AuthEntity> findByToken(String token);
}
