package it.antonio.sp.view;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.logging.log4j.LogManager;
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
    		String fileName = "ReportsBySpecialtyExpired_XLSX.xlsx";
    		String basePath = "C:/anagraficavvf_config/exports/" + fileName;
    		String basePathURL ="<a href=\"" + basePath + "\">" + fileName + "</a>";
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", basePathURL));
    	} catch (Exception e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while saving C:/anagraficavvf_config/exports/ReportsBySpecialtyExpired_XLSX.xlsx"));
    		LogManager.getLogger().info("Error in export ReportByExpiredSpecialty PDF: " + e);
    	}
    }
    
    public void OnExportPdfButtonClicked() {
    	try {
	    	anagraphicService.exportReportsBySpecialtyExpired("pdf");
	    	String fileName = "ReportsBySpecialtyExpired_PDF.pdf";
	    	String basePath = "C:/anagraficavvf_config/exports/" + fileName;
    		String basePathURL ="<a href=\"" + basePath + "\">" + fileName + "</a>";
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", basePathURL));
	    } catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while saving C:/anagraficavvf_config/exports/ReportsBySpecialtyExpired_PDF.pdf"));
			LogManager.getLogger().info("Error in export ReportByExpiredSpecialty PDF: " + e);
	    }
    }	
}
