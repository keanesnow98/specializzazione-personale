package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.SpecialtyService;

@ManagedBean
@ViewScoped
public class ReportsByTurnoView {
	private List<AnagraphicEntity> turnoA;
	private List<AnagraphicEntity> turnoB;
	private List<AnagraphicEntity> turnoC;
	private List<AnagraphicEntity> turnoD;
	private List<AnagraphicEntity> turnoG5;
	private List<AnagraphicEntity> turnoDisc;


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
	public List<AnagraphicEntity> getTurnoDisc() {
		return turnoDisc;
	}

	public void setTurnoDisc(List<AnagraphicEntity> turnoDisc) {
		this.turnoDisc = turnoDisc;
	}
	
	@Autowired
	SpecialtyService specialtyService;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@PostConstruct
	public void init() {
		turnoA = anagraphicService.getFilteredByTurnoA();
		turnoB = anagraphicService.getFilteredByTurnoB();
		turnoC = anagraphicService.getFilteredByTurnoC();
		turnoD = anagraphicService.getFilteredByTurnoD();
		turnoG5 = anagraphicService.getFilteredByTurnoG5();
		turnoDisc = anagraphicService.getFilteredByTurnoDisc();
	}
}
