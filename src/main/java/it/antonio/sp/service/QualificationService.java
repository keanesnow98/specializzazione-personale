package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.QualificationEntity;
import it.antonio.sp.repository.QualificationRepository;
import it.antonio.sp.request.TextRequest;

@Service
public class QualificationService {
	@Autowired
	QualificationRepository qualificationRepository;
	
	public Iterable<QualificationEntity> findAll() {
		return qualificationRepository.findAll().toIterable();
	}
	
	public List<QualificationEntity> findAllEnabled() {
		List<QualificationEntity> enabledQualifications = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDisabled())
				enabledQualifications.add(result);
		});
		return enabledQualifications;
	}
	
	public List<String> getEnabledNames() {
		List<String> enabledNames = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDisabled())
				enabledNames.add(result.getQualificationName());
		});
		return enabledNames;
	}
	
	public QualificationEntity saveQualification(TextRequest textRequest) {
		QualificationEntity qualification;
		if (textRequest.getId() == null)
			qualification = new QualificationEntity();
		else qualification = qualificationRepository.findById(textRequest.getId()).block();
		qualification.setQualificationName(textRequest.getText());
		return qualificationRepository.save(qualification).block();
    }
	
	public void updateById(String id, Document document) {
    	QualificationEntity qualification = qualificationRepository.findById(id).block();
    	if (document.getString("disabled") != null)
    		qualification.setDisabled(document.getString("disabled").equals("true"));
		qualificationRepository.save(qualification).block();
    }
}
