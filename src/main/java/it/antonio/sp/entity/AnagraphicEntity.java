package it.antonio.sp.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class AnagraphicEntity {
	
	public static class SpecialtyExpiration {
		String specialty;
		LocalDate achievedDate;
		Integer validationMonths;
		
		public SpecialtyExpiration() {
			validationMonths = 0;
		}
		
		public String getSpecialty() {
			return specialty;
		}
		
		public void setSpecialty(String specialty) {
			this.specialty = specialty;
		}
		
		public LocalDate getAchievedDate() {
			return achievedDate;
		}
		
		public void setAchievedDate(LocalDate achievedDate) {
			this.achievedDate = achievedDate;
		}
		
		public Integer getValidationMonths() {
			return validationMonths;
		}
		
		public void setValidationMonths(Integer validationMonths) {
			this.validationMonths = validationMonths;
		}
	}
	
	@Id
  	ObjectId id;
	String photo = "default.png";
	String firstName;
	String lastName;
	Integer ruolo;
	String fiscalCode;
	String qualification;
    String turno;
    String phoneNumber;
    String contactEmail = "";
    String note = "";
    
    List<SpecialtyExpiration> specialtyExpirations = new ArrayList<>();
    
    LocalDate lastModifiedAt;
    String lastModifiedBy;
    Boolean deleted = false;
    
    public ObjectId getId() {
		return id;
	}
    
    public void setId(ObjectId id) {
		this.id = id;
	}
    
    public String getPhoto() {
		return photo;
	}
    
    public void setPhoto(String photo) {
		this.photo = photo;
	}
    
    public String getFirstName() {
		return firstName;
	}
    
    public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
    
    public String getLastName() {
		return lastName;
	}
    
    public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    public Integer getRuolo() {
		return ruolo;
	}
    
    public void setRuolo(Integer ruolo) {
		this.ruolo = ruolo;
	}
    
    public String getQualification() {
		return qualification;
	}
    
    public void setQualification(String qualification) {
		this.qualification = qualification;
	}
    
    public String getTurno() {
		return turno;
	}
    
    public void setTurno(String turno) {
		this.turno = turno;
	}
    
    public String getFiscalCode() {
		return fiscalCode;
	}
    
    public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
    
    public String getPhoneNumber() {
		return phoneNumber;
	}
    
    public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
    
    public String getContactEmail() {
		return contactEmail;
	}
    
    public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
    
    public String getNote() {
		return note;
	}
    
    public void setNote(String note) {
		this.note = note;
	}
    
    public List<SpecialtyExpiration> getSpecialtyExpirations() {
		return specialtyExpirations;
	}

    public void setSpecialtyExpirations(List<SpecialtyExpiration> specialtyExpirations) {
		this.specialtyExpirations = specialtyExpirations;
	}
    
    public LocalDate getLastModifiedAt() {
		return lastModifiedAt;
	}
    
    public void setLastModifiedAt(LocalDate lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
    
    public String getLastModifiedBy() {
		return lastModifiedBy;
	}
    
    public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
    
    public Boolean getDeleted() {
		return deleted;
	}
    
    public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
