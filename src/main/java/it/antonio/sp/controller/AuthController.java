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
import it.antonio.sp.service.AuthService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    
    @GetMapping("/login")
    public String loginPage(@ModelAttribute UserEntity user, Model model) {
    	user = new UserEntity();
    	model.addAttribute("user", user);
    	return "/api/auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest req, @ModelAttribute UserEntity user, Model model) {
    	if (authService.login(user, req).blockFirst())
    		return "redirect:/api/dashboard";
    	return loginPage(user, model);
    }
    
    @GetMapping("/register")
    public String registerPage(@ModelAttribute UserEntity user, Model model) {
    	user = new UserEntity();
    	model.addAttribute("user", user);
    	return "/api/auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserEntity user, Model model) {
        if( authService.register(user).blockFirst())
        	return "redirect:/api/auth/login";
        return registerPage(user, model);

    }
    
    @RequestMapping("/logout")
    public String logoutPage(HttpServletRequest req) {
    	authService.logout(req);
    	return "/api/auth/logout";
    }
}
