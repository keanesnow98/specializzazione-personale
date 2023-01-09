package it.antonio.sp.view;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.service.AnagraphicService;

@ManagedBean
@ViewScoped
public class UserDetailsView {
	@Autowired
	AnagraphicService anagraphicService;
	
	private AnagraphicEntity userDetails;
	
	public AnagraphicEntity getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(AnagraphicEntity userDetails) {
		this.userDetails = userDetails;
	}
	
	@PostConstruct
	public void init() {
		userDetails = anagraphicService.getByContactEmail(SecurityContextHolder.getContext().getAuthentication().getName()).blockFirst();
	}
}
