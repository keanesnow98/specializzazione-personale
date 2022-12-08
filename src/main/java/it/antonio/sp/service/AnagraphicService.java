package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.repository.AnagraphicRepository;

@Service
public class AnagraphicService {
	@Autowired
	AnagraphicRepository anagraphicRepository;

	public List<AnagraphicEntity> findAll() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAll().toIterable().forEach(anagraphic -> {
			if (!anagraphic.getDeleted())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyValid() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		findAll().forEach(anagraphic -> {
			if (anagraphic.getSpecialtyExpirations().stream().allMatch(specialtyExp -> specialtyExp.isValid()) && !anagraphic.getSpecialtyExpirations().isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyExpired() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		findAll().forEach(anagraphic -> {
			List<SpecialtyExpiration> newSpecialtyExpirations = new ArrayList<SpecialtyExpiration>();
			anagraphic.getSpecialtyExpirations().forEach(specialtyExpiration -> {
				if (!specialtyExpiration.isValid())
					newSpecialtyExpirations.add(specialtyExpiration);
			});
			anagraphic.setSpecialtyExpirations(newSpecialtyExpirations);
			if (!newSpecialtyExpirations.isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyEmpty() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		findAll().forEach(anagraphic -> {
			if (anagraphic.getSpecialtyExpirations().isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}

	public AnagraphicEntity saveAnagraphic(AnagraphicEntity anagraphic) {
		return anagraphicRepository.save(anagraphic).block();
    }

	public void deleteAnagraphic(AnagraphicEntity anagraphic) {
//		anagraphicRepository.delete(anagraphic).block();
		anagraphic.setDeleted(true);
		anagraphicRepository.save(anagraphic).block();
    }

	public List<Number> getSpecialtyCounts(List<String> specialtyNames) {
		List<Number> specialtyCounts = new ArrayList<Number>(specialtyNames.size());
		for (int i = 0; i < specialtyNames.size(); i++)
			specialtyCounts.add(0);
		for (int i = 0; i < specialtyNames.size(); i++) {
			final int finalI = i;
			findAll().forEach(result -> {
				for (int j = 0; j < result.getSpecialtyExpirations().size(); j++)
					if (result.getSpecialtyExpirations().get(j).getSpecialty().equals(specialtyNames.get(finalI))) {
						specialtyCounts.set(finalI, specialtyCounts.get(finalI).intValue() + 1);
						break;
					}
			});
		}
		return specialtyCounts;
	}
	
	public List<Object> getQualificationCounts(List<String> qualificationNames) {
		List<Object> qualificationCounts = new ArrayList<Object>(qualificationNames.size());
		for (int i = 0; i < qualificationNames.size(); i++)
			qualificationCounts.add(0);
		for (int i = 0; i < qualificationNames.size(); i++) {
			final int finalI = i;
			findAll().forEach(result -> {
				if (result.getQualification().equals(qualificationNames.get(finalI)))
					qualificationCounts.set(finalI, (int) qualificationCounts.get(finalI) + 1);
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
	
	public List<AnagraphicEntity> getFilteredByTurnoA(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("A"))
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
	
	public List<AnagraphicEntity> getFilteredByTurnoB(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("B"))
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
	
	public List<AnagraphicEntity> getFilteredByTurnoC(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("C"))
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
	
	public List<AnagraphicEntity> getFilteredByTurnoD(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("D"))
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
	
	public List<AnagraphicEntity> getFilteredByTurnoG(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().equals("G"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public Integer[] getTurnoACounts() {
		Integer[] turnoACounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoACounts[i] = 0;
		}
		findAll().forEach(anagraphic -> {
			if (anagraphic.getTurno().startsWith("A")) {
				int index = anagraphic.getTurno().codePointAt(1) - 0x31;
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
		findAll().forEach(anagraphic -> {
			if (anagraphic.getTurno().startsWith("B")) {
				int index = anagraphic.getTurno().codePointAt(1) - 0x31;
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
		findAll().forEach(anagraphic -> {
			if (anagraphic.getTurno().startsWith("C")) {
				int index = anagraphic.getTurno().codePointAt(1) - 0x31;
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
		findAll().forEach(anagraphic -> {
			if (anagraphic.getTurno().startsWith("D")) {
				int index = anagraphic.getTurno().codePointAt(1) - 0x31;
				turnoDCounts[index] = turnoDCounts[index] + 1;
			}
		});
		return turnoDCounts;
	}
}
