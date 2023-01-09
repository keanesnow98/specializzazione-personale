package it.antonio.sp.view;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
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
	
	private UploadedFile imageFile;
	private String uploadedImageName;
	
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
	
	public UploadedFile getImageFile() {
		return imageFile;
	}
	
	public String getUploadedImageName() {
		return uploadedImageName;
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
        
        String photoName = selectedAnagraphic.getPhoto(), newPhotoName = photoName;
		if (Files.notExists(Paths.get(photoName.startsWith("sp-temp-") ? "C:/uploads/temp" : "C:/uploads/photo", photoName)))
			newPhotoName = "default.png";
		if (photoName.startsWith("sp-temp-")) {
	        try {
	        	newPhotoName = UUID.randomUUID().toString().replace("-", "") + ".png";
	        	Files.move(Paths.get("C:/uploads/temp/", photoName), Paths.get("C:/uploads/photo/", newPhotoName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
        selectedAnagraphic.setPhoto(newPhotoName);
        if (selectedSpecialtyExpiration != null && (selectedSpecialtyExpiration.getSpecialty() == "" || selectedSpecialtyExpiration.getAchievedDate() == null)) {
        	List<SpecialtyExpiration> specialtyExpirations = selectedAnagraphic.getSpecialtyExpirations();
        	specialtyExpirations.remove(specialtyExpirations.size() - 1);
        	selectedAnagraphic.setSpecialtyExpirations(specialtyExpirations);
        }
        anagraphicService.saveAnagraphic(selectedAnagraphic);

        selectedAnagraphic = new AnagraphicEntity();
        anagraphics = anagraphicService.findAll();
        selectedSpecialtyExpiration = null;
        clearImageFile();
        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics", "inputs:anagraphic-photo");
    }

    public void deleteAnagraphic() {
    	if (selectedAnagraphic.getId() != null) {
	        anagraphics.remove(selectedAnagraphic);
	        anagraphicService.deleteAnagraphic(selectedAnagraphic);
	        selectedAnagraphic = new AnagraphicEntity();
	        selectedSpecialtyExpiration = null;
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Anagraphic Removed"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-anagraphics", "inputs:anagraphic-photo");
    	}
    }
    
    public void clearSelection() {
    	selectedAnagraphic = new AnagraphicEntity();
    	clearImageFile();
    	PrimeFaces.current().ajax().update("inputs:anagraphic-photo");
    }
    
    public void addNewSpecialtyExp() {
    	List<SpecialtyExpiration> specialtyExpirations = selectedAnagraphic.getSpecialtyExpirations();

    	if (selectedSpecialtyExpiration != null && (selectedSpecialtyExpiration.getSpecialty() == "" || selectedSpecialtyExpiration.getAchievedDate() == null)) {
    		return;
    	}
    	selectedSpecialtyExpiration = new SpecialtyExpiration();
    	specialtyExpirations.add(selectedSpecialtyExpiration);
    	PrimeFaces.current().executeScript("PF('dtSpecialtyExpirations').addRow()");
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
    
    public void handleFileUpload(FileUploadEvent event) {
    	clearImageFile();
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            imageFile = file;
            FacesMessage msg = new FacesMessage("Successful", imageFile.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimeFaces.current().ajax().update("dialogs:uploadPanel");
        }
    }
    
    public void clearImageFile() {
    	imageFile = null;
    }

    public void changeImage() {
    	if (imageFile == null
            || imageFile.getContent() == null
            || imageFile.getContent().length == 0) {
            return;
        }

        try {
            BufferedImage dest = ImageIO.read(new ByteArrayInputStream(imageFile.getContent()));

            String filename = "sp-temp";
            Path file = Files.createFile(Paths.get("C:/uploads/temp", filename + "-" + UUID.randomUUID().toString().replace("-", "") + ".png"));
            ImageIO.write(dest, "png", file.toFile());
            
            if (selectedAnagraphic.getPhoto().equals("default.png"))
            	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Photo added"));
            else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Photo changed"));
            
            selectedAnagraphic.setPhoto(file.getFileName().toString());
            PrimeFaces.current().executeScript("PF('imageChangeDialog').hide()");
            PrimeFaces.current().ajax().update(":inputs:anagraphic-photo");
            clearImageFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
