package it.antonio.sp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import it.antonio.sp.entity.SpecialtyEntity;

public interface SpecialtyRepository extends ReactiveCrudRepository<SpecialtyEntity, String> {

}
