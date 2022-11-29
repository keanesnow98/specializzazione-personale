package it.antonio.sp.view;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.QualificationService;

@ManagedBean
@ViewScoped
public class AnagraphicView {
	private List<AnagraphicEntity> anagraphics;
	private List<String> qualifications;
	private AnagraphicEntity selectedAnagraphic;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@Autowired
	QualificationService qualificationService;
	
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
	
	@PostConstruct
	public void init() {
		anagraphics = anagraphicService.findAll();
		qualifications = qualificationService.getQualificationNames();
	}
	
	public List<String> completeQualification(String query) {
        String queryLowerCase = query.toLowerCase();
        return qualifications.stream().filter(t -> t.toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }
	
	public void openNew() {
        selectedAnagraphic = new AnagraphicEntity();
    }
	
	public void saveAnagraphic() {
        if (selectedAnagraphic.getId() == null) {
        	anagraphics.add(selectedAnagraphic);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Added"));
        } else {
        	selectedAnagraphic.setLastModifiedAt(new Date());
        	selectedAnagraphic.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Updated"));
        }
        anagraphicService.saveAnagraphic(selectedAnagraphic);

        PrimeFaces.current().executeScript("PF('manageAnagraphicDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics");
    }

    public void deleteAnagraphic() {
        anagraphics.remove(selectedAnagraphic);
        anagraphicService.deleteAnagraphic(selectedAnagraphic);
        this.selectedAnagraphic = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics");
    }
}
