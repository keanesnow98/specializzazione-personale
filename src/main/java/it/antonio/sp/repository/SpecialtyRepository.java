package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import it.antonio.sp.entity.SpecialtyEntity;

@Repository
public interface SpecialtyRepository extends ReactiveCrudRepository<SpecialtyEntity, String> {

}
