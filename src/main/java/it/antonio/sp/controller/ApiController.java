package it.antonio.sp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.antonio.sp.entity.AnagraphicEntity;
import it.antonio.sp.entity.AnagraphicEntity.SpecialtyExpiration;
import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.request.TextRequest;
import it.antonio.sp.service.AnagraphicService;
import it.antonio.sp.service.AuthService;
import it.antonio.sp.service.QualificationService;
import it.antonio.sp.service.SpecialtyService;
import it.antonio.sp.util.Generator;

@Controller
@RequestMapping("/api")
public class ApiController {

	@Autowired
    AuthService authService;
	
	@Autowired
	AnagraphicService anagraphicService;
	
	@Autowired
	SpecialtyService specialtyService;
	
	@Autowired
	QualificationService qualificationService;
	
	@GetMapping("/dashboard")
	public String dashboardPage(HttpServletRequest req, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		List<String> specialtyNames = specialtyService.getEnabledNames();
		model.addAttribute("specialtyLabels", specialtyNames);
		model.addAttribute("specialtyCounts", anagraphicService.getSpecialtyCounts(specialtyNames));

		List<String> qualificationNames = qualificationService.getEnabledNames();
		model.addAttribute("qualificationLabels", qualificationNames);
		model.addAttribute("qualificationCounts", anagraphicService.getQualificationCounts(qualificationNames));

		model.addAttribute("turnoACounts", anagraphicService.getTurnoACounts());
		model.addAttribute("turnoBCounts", anagraphicService.getTurnoBCounts());
		model.addAttribute("turnoCCounts", anagraphicService.getTurnoCCounts());
		model.addAttribute("turnoDCounts", anagraphicService.getTurnoDCounts());
		return "/api/dashboard";
	}
	
	@GetMapping("/anagraphic")
	public String anagraphicPage(HttpServletRequest req, @ModelAttribute AnagraphicEntity anagrapihcEntity, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		anagrapihcEntity = new AnagraphicEntity();
		model.addAttribute("anagraphic", anagrapihcEntity);
		model.addAttribute("anagraphics", anagraphicService.findAll());
		model.addAttribute("qualifications", qualificationService.findAllEnabled());
		model.addAttribute("specialties", specialtyService.findAllEnabled());
		return "api/anagraphic";
	}
	
	@PostMapping("/anagraphic")
	public String saveAnagraphic(HttpServletRequest req, @ModelAttribute AnagraphicEntity anagraphicEntity, @RequestParam List<String> formSpecialties, @RequestParam List<String> formAchievedDates, @RequestParam List<Integer> formValidationMonths, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		if (formSpecialties.size() > 1 && formSpecialties.size() == formAchievedDates.size() && formAchievedDates.size() == formValidationMonths.size()) {
			List<SpecialtyExpiration> specialtyExpirations = new ArrayList<>();
			for (int i = 1; i < formSpecialties.size(); i++)
				specialtyExpirations.add(new SpecialtyExpiration(formSpecialties.get(i), LocalDate.parse(formAchievedDates.get(i)), formValidationMonths.get(i)));
			anagraphicEntity.setSpecialtyExpirations(specialtyExpirations);
		}

		if (anagraphicService.saveAnagraphic(anagraphicEntity) != null)
			model.addAttribute("success", "Anagraphic successfuly saved!");
		else model.addAttribute("failure", "Saving anagraphic failure!");
		anagraphicEntity = new AnagraphicEntity();
		return anagraphicPage(req, anagraphicEntity, model);
	}

	@PostMapping("/anagraphic/remove/{id}")
	public String deleteAnagraphic(HttpServletRequest req, @PathVariable String id, @ModelAttribute AnagraphicEntity anagraphicEntity, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		anagraphicService.deleteAnagraphic(id);
		anagraphicEntity = new AnagraphicEntity();
		return anagraphicPage(req, anagraphicEntity, model);
	}

	@GetMapping("/reports")
	public String reportsPage(HttpServletRequest req, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		model.addAttribute("turnoA", anagraphicService.getFilteredByTurnoA());
		model.addAttribute("turnoB", anagraphicService.getFilteredByTurnoB());
		model.addAttribute("turnoC", anagraphicService.getFilteredByTurnoC());
		model.addAttribute("turnoD", anagraphicService.getFilteredByTurnoD());
		model.addAttribute("turnoG", anagraphicService.getFilteredByTurnoG());
		return "/api/reports";
	}
	
	@GetMapping("/specialties")
	public String specialtyManagementPage(HttpServletRequest req, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		model.addAttribute("specialties", specialtyService.findAll());
		model.addAttribute("textRequest", textRequest);
		return "/api/specialties";
	}
	
	@PostMapping("/specialties")
	public String saveSpecialty(HttpServletRequest req, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		specialtyService.saveSpecialty(textRequest);
		textRequest = new TextRequest();
		return specialtyManagementPage(req, textRequest, model);
	}
	
	@PostMapping("/specialties/modify/{id}")
	public String modifySpecialtyById(HttpServletRequest req, @PathVariable String id, @RequestBody Document document, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		specialtyService.updateById(id, document);
		textRequest = new TextRequest();
	    return specialtyManagementPage(req, textRequest, model);
	}
	
	@GetMapping("/qualifications")
	public String qualificationManagementPage(HttpServletRequest req, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		model.addAttribute("qualifications", qualificationService.findAll());
		model.addAttribute("textRequest", textRequest);
		return "/api/qualifications";
	}
	
	@PostMapping("/qualifications")
	public String saveQualification(HttpServletRequest req, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		qualificationService.saveQualification(textRequest);
		textRequest = new TextRequest();
		return qualificationManagementPage(req, textRequest, model);
	}
	
	@PostMapping("/qualifications/modify/{id}")
	public String modifyQualificationById(HttpServletRequest req, @PathVariable String id, @RequestBody Document document, @ModelAttribute TextRequest textRequest, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		qualificationService.updateById(id, document);
		textRequest = new TextRequest();
	    return qualificationManagementPage(req, textRequest, model);
	}
	
	@GetMapping("/users")
	public String userManagementPage(HttpServletRequest req, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		model.addAttribute("users", authService.findAll());
		return "/api/users";
	}
	
	@PostMapping("/users/modify/{id}")
	public String modifyUserById(HttpServletRequest req, @PathVariable String id, @RequestBody Document document, Model model) {
		UserEntity user = authService.verifyToken(req).blockFirst();
		if (user == null)
			return "redirect:/api/auth/login";
		else if (!user.getActive())
			return "redirect:/";
		customizeUserProfile(user, model);
		
		authService.updateById(id, document);
	    return userManagementPage(req, model);
	}
	
	void customizeUserProfile(UserEntity user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("gravatarURI", "https://www.gravatar.com/avatar/" + Generator.md5Hex(user.getEmail()) + "?s=80");
	}
}
