package it.antonio.sp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import it.antonio.sp.entity.AnagraphicEntity;
import reactor.core.publisher.Flux;

public interface AnagraphicRepository extends ReactiveCrudRepository<AnagraphicEntity, ObjectId> {
	Flux<AnagraphicEntity> findAllByDeleted(Boolean deleted);
	Flux<AnagraphicEntity> findAllByTurnoAndDeleted(String turno, Boolean deleted);
	Flux<AnagraphicEntity> findAllByTurnoStartsWithAndDeleted(String turno, Boolean deleted);
	Flux<AnagraphicEntity> findAllByContactEmail(String contactEmail);
}
