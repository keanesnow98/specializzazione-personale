package it.antonio.sp.bean;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.web.context.annotation.SessionScope;

@ManagedBean
@SessionScope
public class ImageBean {
	public StreamedContent getImage()
    {
    	String photoName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photo-name");
    	
    	if (Files.notExists(Paths.get(photoName.startsWith("sp-temp-") ? "C:/uploads/temp" : "C:/uploads/photo", photoName)))
    		photoName = "default.png";
    	
    	final String finalPhotoName = photoName;
    	
    	LogManager.getLogger().info("Loading image: " + finalPhotoName);
		
    	return DefaultStreamedContent.builder()
	        .contentType("image/png")
	        .stream(() -> {
	            try {
	            	return new FileInputStream(new File(finalPhotoName.startsWith("sp-temp-") ? "C:/uploads/temp" : "C:/uploads/photo", finalPhotoName));
	            } catch (Exception e) {
	                e.printStackTrace();
	                return null;
	            }
	        })
	        .build();
    }
}
