package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.repository.AnagraphicRepository;

@Service
public class AnagraphicService {
	@Autowired
	AnagraphicRepository anagraphicRepository;
	
	public Iterable<AnagraphicEntity> findAll() {
		return anagraphicRepository.findAll().toIterable();
	}
	
	public AnagraphicEntity saveAnagraphic(AnagraphicEntity anagraphicEntity) {
		return anagraphicRepository.save(anagraphicEntity).block();
    }
	
	public void deleteAnagraphic(String id) {
		anagraphicRepository.deleteById(new ObjectId(id)).block();
    }
	
	public Integer[] getSpecialtyCounts(List<String> specialtyNames) {
 		Iterable<AnagraphicEntity> anagraphics =  findAll();
		Integer[] specialtyCounts = new Integer[specialtyNames.size()];
		for (int i = 0; i < specialtyNames.size(); i++) {

			final int finalI = i;
			specialtyCounts[i] = 0;

			anagraphics.forEach(result -> {
				for (int j = 0; j < result.getSpecialtyExpirations().size(); j++)
					if (result.getSpecialtyExpirations().get(j).getSpecialty().equals(specialtyNames.get(finalI))) {
						specialtyCounts[finalI]++;
						break;
					}
			});
		}
		return specialtyCounts;
	}
	
	public Integer[] getQualificationCounts(List<String> qualificationNames) {
		Iterable<AnagraphicEntity> anagraphics =  findAll();
		Integer[] qualificationCounts = new Integer[qualificationNames.size()];
		for (int i = 0; i < qualificationNames.size(); i++) {

			final int finalI = i;
			qualificationCounts[i] = 0;
			
			anagraphics.forEach(result -> {
				if (result.getQualification().equals(qualificationNames.get(finalI)))
					qualificationCounts[finalI]++;
			});
		}
		return qualificationCounts;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoA() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("A"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoB() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("B"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoC() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("C"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoD() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("D"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoG() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().equals("G"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public Integer[] getTurnoACounts() {
		Integer[] turnoACounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoACounts[i] = 0;
		}
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("A")) {
				int index = result.getTurno().codePointAt(1) - 0x31;
				turnoACounts[index] = turnoACounts[index] + 1;
			}
		});
		return turnoACounts;
	}
	
	public Integer[] getTurnoBCounts() {
		Integer[] turnoBCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoBCounts[i] = 0;
		}
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("B")) {
				int index = result.getTurno().codePointAt(1) - 0x31;
				turnoBCounts[index] = turnoBCounts[index] + 1;
			}
		});
		return turnoBCounts;
	}
	
	public Integer[] getTurnoCCounts() {
		Integer[] turnoCCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoCCounts[i] = 0;
		}
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("C")) {
				int index = result.getTurno().codePointAt(1) - 0x31;
				turnoCCounts[index] = turnoCCounts[index] + 1;
			}
		});
		return turnoCCounts;
	}
	
	public Integer[] getTurnoDCounts() {
		Integer[] turnoDCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoDCounts[i] = 0;
		}
		findAll().forEach(result -> {
			if (result.getTurno().startsWith("D")) {
				int index = result.getTurno().codePointAt(1) - 0x31;
				turnoDCounts[index] = turnoDCounts[index] + 1;
			}
		});
		return turnoDCounts;
	}
}
