package it.antonio.sp.view;

import javax.annotation.ManagedBean;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.annotation.RequestScope;

import it.antonio.sp.util.Generator;

@ManagedBean
@RequestScope
public class TopbarView {
	public String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public String getGravatarURI() {
		return "https://www.gravatar.com/avatar/" + Generator.md5Hex(getEmail()) + "?s=80";
	}
}
