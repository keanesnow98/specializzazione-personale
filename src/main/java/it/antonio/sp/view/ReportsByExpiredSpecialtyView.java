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
import javax.faces.view.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.util.Constants;

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
		final String filename = "ReportsBySpecialtyExpired_XLSX.xlsx";
		
    	try {
    		File file = anagraphicService.exportReportsBySpecialtyExpired("xlsx");
    		
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
    		LogManager.getLogger().info("Error in export ReportByExpiredSpecialty PDF: " + e);
    	}
    }
    
    public void OnExportPdfButtonClicked() {
    	final String filename = "ReportsBySpecialtyExpired_PDF.pdf";
    	
    	try {
	    	File file = anagraphicService.exportReportsBySpecialtyExpired("pdf");
	    	
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
			LogManager.getLogger().info("Error in export ReportByExpiredSpecialty PDF: " + e);
	    }
    }	
}
