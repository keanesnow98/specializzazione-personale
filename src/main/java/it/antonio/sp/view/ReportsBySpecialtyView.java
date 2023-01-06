package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.SpecialtyService;

@ManagedBean
@ViewScoped
public class ReportsBySpecialtyView {
	private List<String> specialties;
	private String selectedSpecialty;
	private List<AnagraphicEntity> turnoA;
	private List<AnagraphicEntity> turnoB;
	private List<AnagraphicEntity> turnoC;
	private List<AnagraphicEntity> turnoD;
	private List<AnagraphicEntity> turnoG5;
	
	public List<String> getSpecialties() {
		return specialties;
	}
	
	public void setSpecialties(List<String> specialties) {
		this.specialties = specialties;
	}
	
	public String getSelectedSpecialty() {
		return selectedSpecialty;
	}
	
	public void setSelectedSpecialty(String selectedSpecialty) {
		this.selectedSpecialty = selectedSpecialty;
	}
	
	public List<AnagraphicEntity> getTurnoA() {
		return turnoA;
	}
	
	public void setTurnoA(List<AnagraphicEntity> turnoA) {
		this.turnoA = turnoA;
	}
	
	public List<AnagraphicEntity> getTurnoB() {
		return turnoB;
	}
	
	public void setTurnoB(List<AnagraphicEntity> turnoB) {
		this.turnoB = turnoB;
	}
	
	public List<AnagraphicEntity> getTurnoC() {
		return turnoC;
	}
	
	public void setTurnoC(List<AnagraphicEntity> turnoC) {
		this.turnoC = turnoC;
	}
	
	public List<AnagraphicEntity> getTurnoD() {
		return turnoD;
	}
	
	public void setTurnoD(List<AnagraphicEntity> turnoD) {
		this.turnoD = turnoD;
	}
	
	public List<AnagraphicEntity> getTurnoG5() {
		return turnoG5;
	}
	
	public void setTurnoG5(List<AnagraphicEntity> turnoG5) {
		this.turnoG5 = turnoG5;
	}
	
	@Autowired
	SpecialtyService specialtyService;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@PostConstruct
	public void init() {
		specialties = specialtyService.getSpecialtyNames();
		selectedSpecialty = specialties.isEmpty() ? "" : specialties.get(0);
		turnoA = anagraphicService.getFilteredByTurnoA(selectedSpecialty);
		turnoB = anagraphicService.getFilteredByTurnoB(selectedSpecialty);
		turnoC = anagraphicService.getFilteredByTurnoC(selectedSpecialty);
		turnoD = anagraphicService.getFilteredByTurnoD(selectedSpecialty);
		turnoG5 = anagraphicService.getFilteredByTurnoG5(selectedSpecialty);
	}
	
	public void handleSpecialtyChange(ValueChangeEvent event) {
		String newValue = (String) event.getNewValue();

		turnoA = anagraphicService.getFilteredByTurnoA(newValue);
		turnoB = anagraphicService.getFilteredByTurnoB(newValue);
		turnoC = anagraphicService.getFilteredByTurnoC(newValue);
		turnoD = anagraphicService.getFilteredByTurnoD(newValue);
		turnoG5 = anagraphicService.getFilteredByTurnoG5(newValue);
		
		PrimeFaces.current().ajax().update("turnoA", "turnoB", "turnoC", "turnoD", "turnoG5");
	}
}
