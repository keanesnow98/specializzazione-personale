package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.SpecialtyEntity;
import it.antonio.sp.service.SpecialtyService;

@ManagedBean
@ViewScoped
public class SpecialtiesView {
	private List<SpecialtyEntity> specialties;
	
	@Autowired
	SpecialtyService specialtyService;
	
	public List<SpecialtyEntity> getSpecialties() {
		return specialties;
	}
	
	public void setSpecialties(List<SpecialtyEntity> specialties) {
		this.specialties = specialties;
	}
	
	@PostConstruct
    public void init() {
        specialties = specialtyService.findAll();
    }
	
	public void addNew() {
		SpecialtyEntity specialty = new SpecialtyEntity();
        specialties.add(specialty);
    }
	
	public void onCellEdit(CellEditEvent<Object> event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
        	String headerText = event.getColumn().getHeaderText();
        	SpecialtyEntity selectedSpecialty = specialties.get(event.getRowIndex());
        	
        	if (headerText.equals("Name")) {
        		selectedSpecialty.setSpecialtyName((String) newValue);
        	}
    		specialtyService.saveSpecialty(selectedSpecialty);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Specialty successfully saved"));
        }
    }
}
