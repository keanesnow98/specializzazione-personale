package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.SpecialtyEntity;
import it.antonio.sp.repository.SpecialtyRepository;
import it.antonio.sp.request.TextRequest;

@Service
public class SpecialtyService {
	@Autowired
	SpecialtyRepository specialtyRepository;
	
	public Iterable<SpecialtyEntity> findAll() {
		return specialtyRepository.findAll().toIterable();
	}
	
	public List<SpecialtyEntity> findAllEnabled() {
		List<SpecialtyEntity> enabledSpecialties = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDisabled())
				enabledSpecialties.add(result);
		});
		return enabledSpecialties;
	}
	
	public List<String> getEnabledNames() {
		List<String> enabledNames = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDisabled())
				enabledNames.add(result.getSpecialtyName());
		});
		return enabledNames;
	}
	
	public SpecialtyEntity saveSpecialty(TextRequest textRequest) {
		SpecialtyEntity specialty;
		if (textRequest.getId() == null)
			specialty = new SpecialtyEntity();
		else specialty = specialtyRepository.findById(textRequest.getId()).block();
		specialty.setSpecialtyName(textRequest.getText());
		return specialtyRepository.save(specialty).block();
    }
	
	public void updateById(String id, Document document) {
		SpecialtyEntity specialty = specialtyRepository.findById(id).block();
    	if (document.getString("disabled") != null)
    		specialty.setDisabled(document.getString("disabled").equals("true"));
		specialtyRepository.save(specialty).block();
    }
}
