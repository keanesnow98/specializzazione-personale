package it.antonio.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user")
public class UserEntity {
	@Id
	String id;
    String email;
    String password;
    String role;
    Boolean active = false;
    Date createdAt;

    public String getId() {
		return id;
	}

    public void setId(String id) {
		this.id = id;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
		return role;
	}
    
    public void setRole(String role) {
		this.role = role;
	}
    
    public Boolean getActive() {
		return active;
	}
    
    public void setActive(Boolean active) {
		this.active = active;
	}

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
