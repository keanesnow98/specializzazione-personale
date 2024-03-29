package it.antonio.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "qualificationList")
public class QualificationEntity {
	@Id
	String qualificationId;

    String qualificationName;
    
    public String getQualificationId() {
		return qualificationId;
	}
    
    public void setQualificationId(String qualificationId) {
		this.qualificationId = qualificationId;
	}
    
    public String getQualificationName() {
		return qualificationName;
	}
    
    public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
	}
}
