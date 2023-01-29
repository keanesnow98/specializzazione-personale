package it.antonio.sp.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.SpecialtyService;
import it.antonio.sp.util.Constants;

import org.apache.logging.log4j.LogManager;

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
	
	public void OnExportXlsxButtonClicked() {
		final String filename = "ReportsBySpecialty(" + selectedSpecialty + ")_XLSX.xlsx";
		
    	try {
    		File file = anagraphicService.exportReportsBySpecialty("xlsx", selectedSpecialty);
    		
    		FacesContext facesContext = FacesContext.getCurrentInstance();
    	    ExternalContext ec = facesContext.getExternalContext();

    	    ec.responseReset();
    	    ec.setResponseContentType(Constants.CONTENT_TYPE_EXCEL);
    	    ec.setResponseContentLength((int) file.length());
    	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    	    OutputStream responseOutputStream = ec.getResponseOutputStream();
    	    
    	    InputStream fileInputStream = new FileInputStream(file);
    	    
    	    byte[] bytesBuffer = new byte[2048];
    	    int bytesRead;
    	    while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0)
    	        responseOutputStream.write(bytesBuffer, 0, bytesRead);

    	    responseOutputStream.flush();

    	    fileInputStream.close();
    	    responseOutputStream.close();

    	    facesContext.responseComplete();
    	    
    	    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", filename + " exported"));
    	} catch (Exception e) {
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while downloading " + filename));
			LogManager.getLogger().info("Error in export ReportBySpecialty XLSX: " + e);
    	}
    }
	
	public void OnExportPdfButtonClicked() {
		final String filename = "ReportsBySpecialty(" + selectedSpecialty + ")_PDF.pdf";
		
    	try {
	    	File file = anagraphicService.exportReportsBySpecialty("pdf", selectedSpecialty);
	    	
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	    ExternalContext ec = facesContext.getExternalContext();

    	    ec.responseReset();
    	    ec.setResponseContentType(Constants.CONTENT_TYPE_PDF);
    	    ec.setResponseContentLength((int) file.length());
    	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    	    OutputStream responseOutputStream = ec.getResponseOutputStream();
    	    
    	    InputStream fileInputStream = new FileInputStream(file);
    	    
    	    byte[] bytesBuffer = new byte[2048];
    	    int bytesRead;
    	    while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0)
    	        responseOutputStream.write(bytesBuffer, 0, bytesRead);

    	    responseOutputStream.flush();

    	    fileInputStream.close();
    	    responseOutputStream.close();

    	    facesContext.responseComplete();
    	    
    	    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", filename + " exported"));
	    } catch (Exception e) {
	    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Failure", "Error while downloading " + filename));
			LogManager.getLogger().info("Error in export ReportBySpecialty PDF: " + e);
	    }
    }
}
