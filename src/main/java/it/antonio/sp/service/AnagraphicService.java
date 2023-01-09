package it.antonio.sp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.repository.AnagraphicRepository;
import it.antonio.sp.util.SimilarSpecializationMapping;
import reactor.core.publisher.Flux;

@Service
public class AnagraphicService {
	@Autowired
	AnagraphicRepository anagraphicRepository;

	public List<AnagraphicEntity> findAll() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(anagraphic -> {
			anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyValid() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(anagraphic -> {
			if (anagraphic.getSpecialtyExpirations().stream().allMatch(specialtyExp -> specialtyExp.isValid()) && !anagraphic.getSpecialtyExpirations().isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyExpired() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(anagraphic -> {
			
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
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(anagraphic -> {
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
	
	public Map<String, Number> getGroupedSpecialtyCounts() {
		Map<String,Number> countsMap = new TreeMap<>();
		
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(result -> {
			for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
				String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
				String mappedSpecialty = SimilarSpecializationMapping.getSimilarMappingIfExist(specialty);
				if (countsMap.containsKey(mappedSpecialty))
					countsMap.put(mappedSpecialty, countsMap.get(mappedSpecialty).intValue() + 1);
				else countsMap.put(mappedSpecialty, 1);
			}
		});
		
		return countsMap;
	}
	
	
	public Map<String, Number> getSingleSpecialtyCounts(String singleSpecialty) {
		Map<String,Number> countsMap = new TreeMap<>();
		
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(result -> {
			for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
				if (result.getSpecialtyExpirations().get(j).getSpecialty().contains(singleSpecialty)) {
					String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
					if (countsMap.containsKey(specialty))
						countsMap.put(specialty, countsMap.get(specialty).intValue() + 1);
					else countsMap.put(specialty, 1);
				}
			}
		});
		
		return countsMap;
	}
	
	public Map<String, Number> getSpecialtyCounts() {
		Map<String,Number> countsMap = new TreeMap<>();
		
		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(result -> {
			for (int j = 0; j < result.getSpecialtyExpirations().size(); j++) {
				String specialty = result.getSpecialtyExpirations().get(j).getSpecialty();
				if (countsMap.containsKey(specialty))
					countsMap.put(specialty, countsMap.get(specialty).intValue() + 1);
				else countsMap.put(specialty, 1);
			}
		});
		
		return countsMap;
	}
	
	public List<Number> getSpecialty2Counts(String specialtyNames) {
		return null;
	}
	
	public Map<String, Number> getQualificationCounts() {
		Map<String, Number> countsMap = new TreeMap<>();

		anagraphicRepository.findAllByDeleted(false).toIterable().forEach(result -> {
			if (countsMap.containsKey(result.getQualification())) {
				int oldValue = countsMap.get(result.getQualification()).intValue();
				countsMap.put(result.getQualification(), oldValue + 1);
			} else countsMap.put(result.getQualification(), 1);
		});	
		
		return countsMap;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoA() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("A", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoA(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("A", false).toIterable().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoB() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("B", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoB(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("B", false).toIterable().forEach(result -> {
			if (!result.getDeleted() && !result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoC() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("C", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoC(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("C", false).toIterable().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoD() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("D", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoD(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("D", false).toIterable().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoG5() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoAndDeleted("G5", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoG5(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoAndDeleted("G5", false).toIterable().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoDisc() {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("Volontario-", false).toIterable().forEach(result -> {
			filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public List<AnagraphicEntity> getFilteredByTurnoDisc(String specialty) {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("Volontario-", false).toIterable().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		return filteredResult;
	}
	
	public Integer[] getTurnoACounts() {
		Integer[] turnoACounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoACounts[i] = 0;
		}
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("A", false).toIterable().forEach(anagraphic -> {
			int index = anagraphic.getTurno().codePointAt(1) - 0x31;
			turnoACounts[index] = turnoACounts[index] + 1;
		});
		return turnoACounts;
	}
	
	public Integer[] getTurnoBCounts() {
		Integer[] turnoBCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoBCounts[i] = 0;
		}
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("B", false).toIterable().forEach(anagraphic -> {
			int index = anagraphic.getTurno().codePointAt(1) - 0x31;
			turnoBCounts[index] = turnoBCounts[index] + 1;
		});
		return turnoBCounts;
	}
	
	public Integer[] getTurnoCCounts() {
		Integer[] turnoCCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoCCounts[i] = 0;
		}
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("C", false).toIterable().forEach(anagraphic -> {
			int index = anagraphic.getTurno().codePointAt(1) - 0x31;
			turnoCCounts[index] = turnoCCounts[index] + 1;
		});
		return turnoCCounts;
	}
	
	public Integer[] getTurnoDCounts() {
		Integer[] turnoDCounts = new Integer[8];
		for (int i = 0; i < 8; i++) {
			turnoDCounts[i] = 0;
		}
		anagraphicRepository.findAllByTurnoStartsWithAndDeleted("D", false).toIterable().forEach(anagraphic -> {
			int index = anagraphic.getTurno().codePointAt(1) - 0x31;
			turnoDCounts[index] = turnoDCounts[index] + 1;
		});
		return turnoDCounts;
	}

	private long getTurnoATotalCounts() {
		return anagraphicRepository.findAllByTurnoStartsWithAndDeleted("A", false).count().block();
	}
	
	private long getTurnoBTotalCounts() {
		return anagraphicRepository.findAllByTurnoStartsWithAndDeleted("B", false).count().block();
	}
	
	private long getTurnoCTotalCounts() {
		return anagraphicRepository.findAllByTurnoStartsWithAndDeleted("C", false).count().block();
	}
	
	private long getTurnoDTotalCounts() {
		return anagraphicRepository.findAllByTurnoStartsWithAndDeleted("D", false).count().block();
	}
	
	private long getTurnoG5Counts() {
		return anagraphicRepository.findAllByTurnoAndDeleted("G5", false).count().block();
	}
	
	private long getTurnoDiscCounts() {
		return anagraphicRepository.findAllByTurnoStartsWithAndDeleted("Volontario-", false).count().block();
	}

	public Integer[] getTurnoABCDG5DiscCounts() {
		Integer totalsTurnABCDG5Disc[] = {
				(int)getTurnoATotalCounts(),
				(int)getTurnoBTotalCounts(),
				(int)getTurnoCTotalCounts(),
				(int)getTurnoDTotalCounts(),
				(int)getTurnoG5Counts(),
				(int)getTurnoDiscCounts()
			};
	
		return totalsTurnABCDG5Disc;
	}
	
	public Flux<AnagraphicEntity> getByContactEmail(String contactEmail) {
		LogManager.getLogger().info("Get user details from: " + contactEmail);
		
		return anagraphicRepository
				.findAllByContactEmail(contactEmail).map(anagraphic -> {return anagraphic;})
				.defaultIfEmpty(new AnagraphicEntity());
	}
}
