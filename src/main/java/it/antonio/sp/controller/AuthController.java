package it.antonio.sp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.response.Response;
import it.antonio.sp.service.AuthService;
import it.antonio.sp.util.Constants;

@Controller
@RequestMapping(Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_AUTH)
public class AuthController {

    @Autowired
    AuthService authService;
    
    @GetMapping(Constants.URI_LOGIN)
    public String loginPage(@ModelAttribute UserEntity user, Model model) {
    	user = new UserEntity();
    	model.addAttribute("user", user);
    	return Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_AUTH + Constants.URI_LOGIN;
    }

    @PostMapping(Constants.URI_LOGIN)
    public String login(HttpServletRequest req, @ModelAttribute UserEntity user, Model model) {
    	Response response = authService.login(user, req).blockFirst();
    	if (response.ok)
    		return "redirect:" + Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_API + Constants.URI_DASHBOARD;
    	
    	user = new UserEntity();
    	model.addAttribute("errorCallback", true);
    	model.addAttribute("errorMessage", response.errorMessage);
    	return loginPage(user, model);
    }
    
    @GetMapping(Constants.URI_REGISTER)
    public String registerPage(@ModelAttribute UserEntity user, Model model) {
    	user = new UserEntity();
    	model.addAttribute("user", user);
    	return Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_AUTH + Constants.URI_REGISTER;
    }

    @PostMapping(Constants.URI_REGISTER)
    public String register(@ModelAttribute UserEntity user, Model model) {
    	Response response = authService.register(user).blockFirst();
        if(response.ok)
        	return "redirect:" + Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_AUTH + Constants.URI_LOGIN;
        
        user = new UserEntity();
    	model.addAttribute("errorCallback", true);
    	model.addAttribute("errorMessage", response.errorMessage);
        return registerPage(user, model);
    }

    @RequestMapping(Constants.URI_LOGOUT)
    public String logoutPage(HttpServletRequest req) {
    	authService.logout(req);
    	return Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_AUTH + Constants.URI_LOGOUT;
    }
}
