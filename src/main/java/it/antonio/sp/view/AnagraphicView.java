package it.antonio.sp.view;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.QualificationService;
import it.antonio.sp.service.SpecialtyService;

@ManagedBean
@ViewScoped
public class AnagraphicView {
	private List<AnagraphicEntity> anagraphics;
	private AnagraphicEntity selectedAnagraphic;
	private SpecialtyExpiration selectedSpecialtyExpiration;
	private List<String> qualificationNames;
	private List<String> specialtyNames;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@Autowired
	QualificationService qualificationService;
	
	@Autowired
	SpecialtyService specialtyService;
	
	public List<AnagraphicEntity> getAnagraphics() {
		return anagraphics;
	}
	
	public void setAnagraphics(List<AnagraphicEntity> anagraphics) {
		this.anagraphics = anagraphics;
	}
	
	public AnagraphicEntity getSelectedAnagraphic() {
		return selectedAnagraphic;
	}
	
	public void setSelectedAnagraphic(AnagraphicEntity selectedAnagraphic) {
		this.selectedAnagraphic = selectedAnagraphic;
	}
	
	public SpecialtyExpiration getSelectedSpecialtyExpiration() {
		return selectedSpecialtyExpiration;
	}
	
	public void setSelectedSpecialtyExpiration(SpecialtyExpiration selectedSpecialtyExpiration) {
		this.selectedSpecialtyExpiration = selectedSpecialtyExpiration;
	}
	
	public List<String> getQualificationNames() {
		return qualificationNames;
	}
	
	public void setQualificationNames(List<String> qualificationNames) {
		this.qualificationNames = qualificationNames;
	}
	
	public List<String> getSpecialtyNames() {
		return specialtyNames;
	}
	
	public void setSpecialtyNames(List<String> specialtyNames) {
		this.specialtyNames = specialtyNames;
	}

	@PostConstruct
	public void init() {
		selectedAnagraphic = new AnagraphicEntity();
		anagraphics = anagraphicService.findAll();
		qualificationNames = qualificationService.getQualificationNames();
		specialtyNames = specialtyService.getSpecialtyNames();
	}
	
	public void saveAnagraphic() {
        if (selectedAnagraphic.getId() == null) {
        	anagraphics.add(selectedAnagraphic);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Added"));
        } else {
        	selectedAnagraphic.setLastModifiedAt(LocalDate.now());
        	selectedAnagraphic.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Updated"));
        }
        anagraphicService.saveAnagraphic(selectedAnagraphic);

        selectedAnagraphic = new AnagraphicEntity();
        selectedSpecialtyExpiration = null;
        PrimeFaces.current().executeScript("PF('manageAnagraphicDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics");
    }

    public void deleteAnagraphic() {
    	if (selectedAnagraphic.getId() != null) {
	        anagraphics.remove(selectedAnagraphic);
	        anagraphicService.deleteAnagraphic(selectedAnagraphic);
	        selectedAnagraphic = new AnagraphicEntity();
	        selectedSpecialtyExpiration = null;
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Removed"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics");
    	}
    }
    
    public void clearSelection() {
    	selectedAnagraphic = new AnagraphicEntity();
    }
    
    public void addNewSpecialtyExp() {
    	List<SpecialtyExpiration> specialtyExpirations = selectedAnagraphic.getSpecialtyExpirations();
    	specialtyExpirations.add(new SpecialtyExpiration());
    }
    
    public void onSpecialtyExpEdit(CellEditEvent<Object> event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
        	selectedSpecialtyExpiration = selectedAnagraphic.getSpecialtyExpirations().get(event.getRowIndex());
        	PrimeFaces.current().ajax().update("inputs:dt-specialty-expirations");
        }
    }
    
    public void deleteSpecialtyExp() {
    	if (selectedSpecialtyExpiration != null) {
    		selectedAnagraphic.getSpecialtyExpirations().remove(selectedSpecialtyExpiration);
    		selectedSpecialtyExpiration = null;
    		PrimeFaces.current().ajax().update("inputs:dt-specialty-expirations");
    	}
    }
}
