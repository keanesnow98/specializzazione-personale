package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.SpecialtyEntity;
import it.antonio.sp.repository.SpecialtyRepository;

@Service
public class SpecialtyService {
	@Autowired
	SpecialtyRepository specialtyRepository;
	
	public List<SpecialtyEntity> findAll() {
		List<SpecialtyEntity> specialties = new ArrayList<SpecialtyEntity>();
		specialtyRepository.findAll().toIterable().forEach(specialty -> {
			specialties.add(specialty);
		});
		return specialties;
	}
	
	public List<String> getSpecialtyNames() {
		List<String> specialtyNames = new ArrayList<>();
		specialtyRepository.findAll().toIterable().forEach(specialty -> {
			specialtyNames.add(specialty.getSpecialtyName());
		});
		return specialtyNames;
	}

	public void saveSpecialty(SpecialtyEntity specialty) {
		specialtyRepository.save(specialty).block();
    }
}
