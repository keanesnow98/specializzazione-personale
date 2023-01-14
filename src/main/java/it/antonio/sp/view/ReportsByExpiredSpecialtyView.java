package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;

@ManagedBean
@ViewScoped
public class ReportsByExpiredSpecialtyView {
	private List<AnagraphicEntity> anagraphics;

	public List<AnagraphicEntity> getAnagraphics() {
		return anagraphics;
	}
	
	public void setAnagraphics(List<AnagraphicEntity> anagraphics) {
		this.anagraphics = anagraphics;
	}
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@PostConstruct
	public void init() {
		anagraphics = anagraphicService.findAllSpecialtyExpired();
	}
	
	public void OnExportXlsxButtonClicked() {
    	try {
    		anagraphicService.exportReportsBySpecialtyExpired("xlsx");
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "ReportsBySpecialtyExpired_XLSX.xlsx exported to C:/exports"));
    	} catch (Exception e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while saving C:/exports/ReportsBySpecialtyExpired_XLSX.xlsx"));
    	}
    }
    
    public void OnExportPdfButtonClicked() {
    	try {
	    	anagraphicService.exportReportsBySpecialtyExpired("pdf");
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "ReportsBySpecialtyExpired_PDF.pdf exported to C:/exports"));
	    } catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while saving C:/exports/ReportsBySpecialtyExpired_PDF.pdf"));
		}
    }	
}
