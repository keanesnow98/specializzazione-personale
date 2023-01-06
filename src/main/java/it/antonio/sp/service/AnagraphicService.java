package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.repository.AnagraphicRepository;
import it.antonio.sp.util.SimilarSpecializationMapping;

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
	
	public List<AnagraphicEntity> findUserDetails(String userLoggedIntoPlatform) {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		
		anagraphicRepository.findAll().toIterable().forEach(anagraphic -> {
			if (!anagraphic.getDeleted() && anagraphic.getContactEmail().equals(userLoggedIntoPlatform))
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyValid() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		findAll().forEach(anagraphic -> {
			if (!anagraphic.getDeleted() && anagraphic.getSpecialtyExpirations().stream().allMatch(specialtyExp -> specialtyExp.isValid()) && !anagraphic.getSpecialtyExpirations().isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyExpired() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		findAll().forEach(anagraphic -> {
			List<SpecialtyExpiration> newSpecialtyExpirations = new ArrayList<SpecialtyExpiration>();
			anagraphic.getSpecialtyExpirations().forEach(specialtyExpiration -> {
				if (!anagraphic.getDeleted() && !specialtyExpiration.isValid())
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
			if (!anagraphic.getDeleted() && anagraphic.getSpecialtyExpirations().isEmpty())
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
	
	public Map<String, Number> getGroupedSpecialtyCounts() {
		Map<String,Number> countsMap = new TreeMap<>();
		
		findAll().forEach(result -> {
			if (!result.getDeleted()) {
				for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
					String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
					String mappedSpecialty = SimilarSpecializationMapping.getSimilarMappingIfExist(specialty);
					if (countsMap.containsKey(mappedSpecialty)) {
						countsMap.put(mappedSpecialty, countsMap.get(mappedSpecialty).intValue() + 1);
					} else {
						countsMap.put(mappedSpecialty, 1);
					}
				}	
			}
		});
		
		return countsMap;
	}
	
	
	public Map<String, Number> getSingleSpecialtyCounts(String singleSpecialty) {
		Map<String,Number> countsMap = new TreeMap<>();
		
		findAll().forEach(result -> {
			if (!result.getDeleted()) {
				for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
					if (result.getSpecialtyExpirations().get(j).getSpecialty().contains(singleSpecialty)) {
						String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
						if (countsMap.containsKey(specialty)) {
							countsMap.put(specialty, countsMap.get(specialty).intValue() + 1);
						} else {
							countsMap.put(specialty, 1);
						}
					}
				}	
			}
		});
		
		return countsMap;
	}
	
	public Map<String, Number> getSpecialtyCounts() {
		Map<String,Number> countsMap = new TreeMap<>();
		
		findAll().forEach(result -> {
			if (!result.getDeleted()) {
				for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
					String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
					if (countsMap.containsKey(specialty)) {
						countsMap.put(specialty, countsMap.get(specialty).intValue() + 1);
					} else {
						countsMap.put(specialty, 1);
					}
				}	
			}
		});
		
		return countsMap;
	}
	
	public List<Number> getSpecialty2Counts(String specialtyNames) {
		return null;
	}
	
	public Map<String, Number> getQualificationCounts() {
		Map<String, Number> countsMap = new TreeMap<>();

		findAll().forEach(result -> {
			if (!result.getDeleted()) {
				if (countsMap.containsKey(result.getQualification())) {
					int oldValue = countsMap.get(result.getQualification()).intValue();
					countsMap.put(result.getQualification(), oldValue + 1);
				} else {
					countsMap.put(result.getQualification(), 1);
				}
			}
		});	
		
		return countsMap;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoA() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && result.getTurno().startsWith("A"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoA(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("A"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoB() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && result.getTurno().startsWith("B"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoB(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().startsWith("B"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoC() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && result.getTurno().startsWith("C"))
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
	
	public List<AnagraphicEntity> getFilteredByTurnoG5() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().equals("G5"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoG5(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().equals("G5"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoDisc() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (result.getTurno().equals("Volontario-A")||
					result.getTurno().equals("Volontario-B") || 
					result.getTurno().equals("Volontario-C") || 
					result.getTurno().equals("Volontario-D"))
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoDisc(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		findAll().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty() && result.getTurno().equals("Volontario-"))
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
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("A")) {
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
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("B")) {
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
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("C")) {
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
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("D")) {
				int index = anagraphic.getTurno().codePointAt(1) - 0x31;
				turnoDCounts[index] = turnoDCounts[index] + 1;
			}
		});
		return turnoDCounts;
	}
	
	public Integer getTurnoG5Counts() {
		AtomicInteger counts = new AtomicInteger(0);
		anagraphicRepository.findAll().toIterable().forEach(anagraphic -> {
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("G5")) {
				counts.incrementAndGet();
			}
		});
		return counts.intValue();
	}
	
	public Integer getTurnoDiscCounts() {
		AtomicInteger counts = new AtomicInteger(0);
		anagraphicRepository.findAll().toIterable().forEach(anagraphic -> {
			if (!anagraphic.getDeleted() && anagraphic.getTurno().startsWith("Volontario-")) {
				counts.incrementAndGet();
			}
		});
		return counts.intValue();
	}

	public Integer[] getTurnoABCDG5DiscCounts() {
		
		Integer[] turnoA = getTurnoACounts();
		int sumA = 0;
		for (int i = 0; i < turnoA.length; i++)
			sumA += turnoA[i];

		Integer[] turnoB = getTurnoBCounts();
		int sumB = 0;
		for (int i = 0; i < turnoB.length; i++)
			sumB += turnoB[i];
		
		Integer[] turnoC = getTurnoCCounts();
		int sumC = 0;
		for (int i = 0; i < turnoC.length; i++)
			sumC += turnoC[i];
		
		Integer[] turnoD = getTurnoDCounts();
		int sumD = 0;
		for (int i = 0; i < turnoD.length; i++)
			sumD += turnoD[i];
		
		int sumG5 = getTurnoG5Counts();

		int sumDisc = getTurnoDiscCounts();
		
		Integer totalsTurnABCDG5Disc[] = {sumA, sumB, sumC, sumD, sumG5, sumDisc};
	
		return totalsTurnABCDG5Disc;
	}
}
