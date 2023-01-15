package it.antonio.sp.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.export.AnagraphicExport;
import it.antonio.sp.repository.AnagraphicRepository;
import it.antonio.sp.util.SimilarSpecializationMapping;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AnagraphicService {
	@Autowired
	AnagraphicRepository anagraphicRepository;

	public List<AnagraphicEntity> findAll() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
			anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> safeFindAll() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
			if (Files.notExists(Paths.get("C:/uploads/photo", anagraphic.getPhoto())))
				anagraphic.setPhoto("default.png");
			anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyValid() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
			if (anagraphic.getSpecialtyExpirations().stream().allMatch(specialtyExp -> specialtyExp.isValid()) && !anagraphic.getSpecialtyExpirations().isEmpty())
				anagraphics.add(anagraphic);
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyExpired() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
			
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
	
	public List<AnagraphicEntity> safeFindAllSpecialtyExpired() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
			
			List<SpecialtyExpiration> newSpecialtyExpirations = new ArrayList<SpecialtyExpiration>();
			anagraphic.getSpecialtyExpirations().forEach(specialtyExpiration -> {
				if (!specialtyExpiration.isValid())
					newSpecialtyExpirations.add(specialtyExpiration);
			});
			anagraphic.setSpecialtyExpirations(newSpecialtyExpirations);
			if (!newSpecialtyExpirations.isEmpty()) {
				if (Files.notExists(Paths.get("C:/uploads/photo", anagraphic.getPhoto())))
					anagraphic.setPhoto("default.png");
				anagraphics.add(anagraphic);
			}
		});
		return anagraphics;
	}
	
	public List<AnagraphicEntity> findAllSpecialtyEmpty() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(anagraphic -> {
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
		
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(result -> {
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
		
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(result -> {
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
		
		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(result -> {
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

		anagraphicRepository.findAllByDeletedOrderByFirstName(false).toIterable().forEach(result -> {
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
	
	public List<AnagraphicEntity> safeGetOrderedByTurno() {
		List<AnagraphicEntity> anagraphics = new ArrayList<AnagraphicEntity>();
		anagraphicRepository.findAllByDeletedOrderByTurno(false).toIterable().forEach(anagraphic -> {
			if (Files.notExists(Paths.get("C:/uploads/photo", anagraphic.getPhoto())))
				anagraphic.setPhoto("default.png");
			anagraphics.add(anagraphic);
		});
		return anagraphics;
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
	
	public Mono<AnagraphicEntity> getById(ObjectId id) {
		return anagraphicRepository.findById(id).map(anagraphic -> {return anagraphic;})
				.defaultIfEmpty(new AnagraphicEntity());
	}
	
	public Flux<AnagraphicEntity> getByContactEmail(String contactEmail) {
		LogManager.getLogger().info("Get user details from: " + contactEmail);
		
		return anagraphicRepository
				.findAllByContactEmail(contactEmail).map(anagraphic -> {return anagraphic;})
				.defaultIfEmpty(new AnagraphicEntity());
	}
	
	public void exportAnagraphicVVF(String reportFormat) throws FileNotFoundException, JRException {
		File file = ResourceUtils.getFile("classpath:reports/anagraphicvvf.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(safeFindAll());
		
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		if (reportFormat.equalsIgnoreCase("xlsx")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/exports/AnagraphicVVF_XLSX.xlsx"));
			
			exporter.exportReport();
		} else if (reportFormat.equalsIgnoreCase("pdf"))
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/exports/AnagraphicVVF_PDF.pdf");
	}
	
	public void exportReportsByTurno(String reportFormat) throws FileNotFoundException, JRException {
		File file = ResourceUtils.getFile("classpath:reports/anagraphicvvf.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(safeGetOrderedByTurno());
		
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		if (reportFormat.equalsIgnoreCase("xlsx")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/exports/ReportsByTurno_XLSX.xlsx"));
			
			exporter.exportReport();
		} else if (reportFormat.equalsIgnoreCase("pdf"))
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/exports/ReportsByTurno_PDF.pdf");
	}
	
	public void exportReportsBySpecialty(String reportFormat, String specialty) throws FileNotFoundException, JRException {
		List<AnagraphicEntity> filteredResult = new ArrayList<>();
		safeGetOrderedByTurno().forEach(result -> {
			if (!result.getSpecialtyExpirations().stream().filter(t -> t.getSpecialty().equals(specialty)).collect(Collectors.toList()).isEmpty())
				filteredResult.add(result);
		});
		
		File file = ResourceUtils.getFile("classpath:reports/anagraphicvvf.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(filteredResult);
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("specialty", specialty);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		if (reportFormat.equalsIgnoreCase("xlsx")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/exports/ReportsBySpecialty(" + specialty + ")_XLSX.xlsx"));
			
			exporter.exportReport();
		} else if (reportFormat.equalsIgnoreCase("pdf"))
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/exports/ReportsBySpecialty(" + specialty + ")_PDF.pdf");
	}
	
	public void exportReportsBySpecialtyExpired(String reportFormat) throws FileNotFoundException, JRException {
		List<AnagraphicExport> exportResult = new ArrayList<>();
		safeFindAllSpecialtyExpired().forEach(result -> {
			result.getSpecialtyExpirations().forEach(t -> {
				AnagraphicExport anagraphicExport = new AnagraphicExport();
				anagraphicExport.setFirstName(result.getFirstName());
				anagraphicExport.setLastName(result.getLastName());
				anagraphicExport.setRuolo(result.getRuolo());
				anagraphicExport.setFiscalCode(result.getFiscalCode());
				anagraphicExport.setQualification(result.getQualification());
				anagraphicExport.setTurno(result.getTurno());
				anagraphicExport.setPhoneNumber(result.getPhoneNumber());
				anagraphicExport.setContactEmail(result.getContactEmail());
				anagraphicExport.setPhoto(result.getPhoto());
				anagraphicExport.setSpecialty(t.getSpecialty());
				anagraphicExport.setExpiredDate(t.getAchievedDate().plusMonths(t.getValidationMonths()).toString());
				exportResult.add(anagraphicExport);
			});
		});
		
		File file = ResourceUtils.getFile("classpath:reports/reports_expired.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(exportResult);
		
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		
		if (reportFormat.equalsIgnoreCase("xlsx")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("C:/exports/ReportsBySpecialtyExpired_XLSX.xlsx"));
			
			exporter.exportReport();
		} else if (reportFormat.equalsIgnoreCase("pdf"))
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/exports/ReportsBySpecialtyExpired_PDF.pdf");
	}
}
