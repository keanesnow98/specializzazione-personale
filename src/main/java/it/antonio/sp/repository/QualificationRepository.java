package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import it.antonio.sp.entity.QualificationEntity;

public interface QualificationRepository extends ReactiveCrudRepository<QualificationEntity, String> {

}
