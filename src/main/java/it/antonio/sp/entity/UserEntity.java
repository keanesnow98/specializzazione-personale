package it.antonio.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Document(collection = "userList")
public class UserEntity {
	@Id
	String id;
    String email;
    String password;
    List<String> roles;
    Boolean active;
    
    public UserEntity() {
    	roles = new ArrayList<String>();
    	active = false;
    }
    
    public UserEntity(String ...roles) {
    	this.roles = Arrays.asList(roles);
    	active = false;
    }

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
    
    public List<String> getRoles() {
		return roles;
	}
    
    public void setRoles(List<String> roles) {
		this.roles = roles;
	}
    
    public Boolean getActive() {
		return active;
	}
    
    public void setActive(Boolean active) {
		this.active = active;
	}
}
