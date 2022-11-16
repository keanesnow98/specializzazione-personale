package it.antonio.sp.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authToken")
public class AuthEntity {
	@Id
    String userId;
            
    String token;

    Date expiredOn, createdAt;
    
    public String getUserId() {
		return userId;
	}
    
    public void setUserId(String userId) {
		this.userId = userId;
	}

    public Date getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(Date expiredOn) {
        this.expiredOn = expiredOn;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
