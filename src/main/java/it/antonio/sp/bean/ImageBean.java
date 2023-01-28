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
    	
    	if (Files.notExists(Paths.get(photoName.startsWith("sp-temp-") ? "C:/anagraficavvf_config/temp" : "C:/anagraficavvf_config/photo", photoName)))
    		photoName = "default.png";
    	
    	final String finalPhotoName = photoName;
    	
    	LogManager.getLogger().info("Loading image: " + finalPhotoName);
		
    	return DefaultStreamedContent.builder()
	        .contentType("image/png")
	        .stream(() -> {
	            try {
	            	return new FileInputStream(new File(finalPhotoName.startsWith("sp-temp-") ? "C:/anagraficavvf_config/temp" : "C:/anagraficavvf_config/photo", finalPhotoName));
	            } catch (Exception e) {
	                e.printStackTrace();
	                return null;
	            }
	        })
	        .build();
    }
	
	
	public StreamedContent getGradeImage() {
		String gradePhotoTmp = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("grade-photo");

		String gradePhoto = gradePhotoTmp.replace(" ", "-").toLowerCase()+".png";
		
		if (Files.notExists(Paths.get("C:/anagraficavvf_config/loghigradi", gradePhoto)))
			gradePhoto = "default-null-grade.png";
		
		final String finalGradePhoto = gradePhoto;
		
		LogManager.getLogger().info("Loading grade image: " + finalGradePhoto);
		
		return DefaultStreamedContent.builder()
		    .contentType("image/png")
		    .stream(() -> {
		        try {
		        	return new FileInputStream(new File(finalGradePhoto.startsWith("sp-temp-") ? "C:/anagraficavvf_config/loghigradi" : "C:/anagraficavvf_config/loghigradi", finalGradePhoto));
		        } catch (Exception e) {
		            e.printStackTrace();
		            return null;
		        }
		    })
		    .build();
	 }
	
	
	public StreamedContent getPatchImage() {
		String patchPhotoTmp = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("patch-photo");

		String patchPhoto = patchPhotoTmp.replace(" ", "-").toLowerCase()+".png";
		if (patchPhoto.contains("vice")){
			patchPhoto = "patch-vigili-del-fuoco.png";
		}else if (patchPhoto.contains("direttore")) {
			patchPhoto = "patch-vigili-del-fuoco.png";
		}else if (patchPhoto.contains("dirigente")) {
			patchPhoto = "patch-vigili-del-fuoco.png";
		}else if (patchPhoto.contains("allievo")) {
			patchPhoto = "default-null-grade.png";
		}else if (patchPhoto.contains("vigile-del-fuoco")) {
			patchPhoto = "patch-vigili-del-fuoco.png";
		}else if (patchPhoto.contains("capo-squadra")) {
			patchPhoto = "patch-capo-squadra.png";
		}else if (patchPhoto.contains("capo-reparto")) {
			patchPhoto = "patch-capo-reparto.png";
		}else if (patchPhoto.contains("ispettore")) {
			patchPhoto = "patch-ispettore.png";
		} else {
			patchPhoto = "default-null-grade.png";
		}
		
		if (Files.notExists(Paths.get("C:/anagraficavvf_config/loghigradi", patchPhoto)))
			patchPhoto = "default-null-grade.png";
		
		final String finalGradePhoto = patchPhoto;
		
		LogManager.getLogger().info("Loading grade image: " + finalGradePhoto);
		
		return DefaultStreamedContent.builder()
		    .contentType("image/png")
		    .stream(() -> {
		        try {
		        	return new FileInputStream(new File(finalGradePhoto.startsWith("sp-temp-") ? "C:/anagraficavvf_config/loghigradi" : "C:/anagraficavvf_config/loghigradi", finalGradePhoto));
		        } catch (Exception e) {
		            e.printStackTrace();
		            return null;
		        }
		    })
		    .build();
	 }
}
