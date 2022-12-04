package it.antonio.sp.view;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import it.antonio.sp.response.Response;
import it.antonio.sp.service.UserService;

@ManagedBean
@RequestScope
public class RegisterView {
	@Autowired
	UserService userService;
	
	private String email;
	private String password;
	private String confirmPassword;
	
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
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public void register() {
		Response response = userService.register(email, password).blockFirst();
		if (response.ok) {
			PrimeFaces.current().executeScript("PF('registerSuccessDialog').show()");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Exist", response.errorMessage));
		}
	}
	
	public void proceed() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
