package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.QualificationEntity;
import it.antonio.sp.repository.QualificationRepository;

@Service
public class QualificationService {
	@Autowired
	QualificationRepository qualificationRepository;
	
	public List<QualificationEntity> findAll() {
		List<QualificationEntity> qualifications = new ArrayList<QualificationEntity>();
		qualificationRepository.findAll().toIterable().forEach(qualification -> {
			qualifications.add(qualification);
		});
		return qualifications;
	}
	
	public List<String> getQualificationNames() {
		List<String> qualificationNames = new ArrayList<>();
		qualificationRepository.findAll().toIterable().forEach(qualification -> {
			qualificationNames.add(qualification.getQualificationName());
		});
		Collections.sort(qualificationNames);
		return qualificationNames;
	}
	
	public void saveQualification(QualificationEntity qualification) {
		qualificationRepository.save(qualification).block();
    }
}
