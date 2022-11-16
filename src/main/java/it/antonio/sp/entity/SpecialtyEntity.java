package it.antonio.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "specialtyList")
public class SpecialtyEntity {
	@Id
	String specialtyId;
            
    String specialtyName;

    Boolean disabled = false;
    
    public String getSpecialtyId() {
		return specialtyId;
	}
    
    public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}
    
    public String getSpecialtyName() {
		return specialtyName;
	}
    
    public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}
    
    public Boolean getDisabled() {
		return disabled;
	}
    
    public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
}
