package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import it.antonio.sp.entity.QualificationEntity;

@Repository
public interface QualificationRepository extends ReactiveCrudRepository<QualificationEntity, String> {

}
