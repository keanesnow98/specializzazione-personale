package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.QualificationEntity;
import it.antonio.sp.service.QualificationService;

@ManagedBean
@ViewScoped
public class QualificationsView {
	private List<QualificationEntity> qualifications;
	
	@Autowired
	QualificationService qualificationService;
	
	public List<QualificationEntity> getQualifications() {
		return qualifications;
	}
	
	public void setQualifications(List<QualificationEntity> qualifications) {
		this.qualifications = qualifications;
	}
	
	@PostConstruct
    public void init() {
        qualifications = qualificationService.findAll();
    }
	
	public void addNew() {
		QualificationEntity qualification = new QualificationEntity();
        qualifications.add(qualification);
    }
	
	public void onCellEdit(CellEditEvent<Object> event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && newValue != "" && !newValue.equals(oldValue)) {
        	String headerText = event.getColumn().getHeaderText();
        	QualificationEntity selectedQualification = qualifications.get(event.getRowIndex());
        	
        	if (headerText.equals("Name")) {
        		selectedQualification.setQualificationName((String) newValue);
        	}
    		qualificationService.saveQualification(selectedQualification);
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Qualification successfully saved"));
        }
    }
}
