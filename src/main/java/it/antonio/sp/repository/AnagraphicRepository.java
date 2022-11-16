package it.antonio.sp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import it.antonio.sp.entity.AnagraphicEntity;

@Repository
public interface AnagraphicRepository extends ReactiveCrudRepository<AnagraphicEntity, ObjectId> {
}
