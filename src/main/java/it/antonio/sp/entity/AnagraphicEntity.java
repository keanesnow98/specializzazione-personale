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
			validationMonths = 6;
		}
		
		public SpecialtyExpiration(String specialty, LocalDate achievedDate, Integer validationMonths) {
			this.specialty = specialty;
			this.achievedDate = achievedDate;
			this.validationMonths = validationMonths;
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
	String qualification;
    String lastName;
    String firstName;
    String turno;
    String fiscalCode;
    String phoneNumber;
    String contactEmail = "";
    
    List<SpecialtyExpiration> specialtyExpirations = new ArrayList<>();
    
    public ObjectId getId() {
		return id;
	}
    
    public void setId(ObjectId id) {
		this.id = id;
	}
    
    public String getQualification() {
		return qualification;
	}
    
    public void setQualification(String qualification) {
		this.qualification = qualification;
	}
    
    public String getLastName() {
		return lastName;
	}
    
    public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    public String getFirstName() {
		return firstName;
	}
    
    public void setFirstName(String firstName) {
		this.firstName = firstName;
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
    
    public List<SpecialtyExpiration> getSpecialtyExpirations() {
		return specialtyExpirations;
	}

    public void setSpecialtyExpirations(List<SpecialtyExpiration> specialtyExpirations) {
		this.specialtyExpirations = specialtyExpirations;
	}
}
