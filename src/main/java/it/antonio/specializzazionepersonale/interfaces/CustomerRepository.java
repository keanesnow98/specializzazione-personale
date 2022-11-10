package it.antonio.specializzazionepersonale.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.antonio.specializzazionepersonale.model.AnagraficaPersonaleVVFModel;

	public interface CustomerRepository extends MongoRepository<AnagraficaPersonaleVVFModel, Integer> {
}