package it.antonio.specializzazionepersonale.controller;

import static com.mongodb.client.model.Filters.eq;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.antonio.specializzazionepersonale.dto.AnagraficaPersonaleVVFDTO;
import it.antonio.specializzazionepersonale.interfaces.CustomerRepository;
import it.antonio.specializzazionepersonale.menu.Menu;
import it.antonio.specializzazionepersonale.model.AnagraficaPersonaleVVFModel;
import it.antonio.specializzazionepersonale.model.LoginCredentials;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController extends Menu {
	  @Autowired
	  private CustomerRepository repo;
	  
	  @GetMapping("/")
	  public String indexLoginForm(Model model) {
	    model.addAttribute("logincredentials", new LoginCredentials());
	    System.out.println("indexLogin");
	    return "login";
	  }
	  
	  @GetMapping("/logout")
	  public String loginForm(Model model) {
	    model.addAttribute("logincredentials", new LoginCredentials());
	    System.out.println("logout");
	    return "login";
	  }
	  
	  @GetMapping("/home")
	  public String home(@ModelAttribute LoginCredentials logincredentials, Model model) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(12);
		data.add(19);
		data.add(3);
		data.add(5);
		data.add(2);
		data.add(3);
			
		model.addAttribute("logincredentials", logincredentials);
		model.addAttribute("data", data);
	    model.addAttribute("menu", Menu.menu("home"));
	    System.out.println("home");
	    return "home";
	  }
	  
	  //Load Page Anagrafica
	  @GetMapping("/anagraficavvf")
	  public String anagraficaVVF(Model model) throws SocketTimeoutException {
	    model.addAttribute("menu", Menu.menu("anagraficavvf"));
	    model.addAttribute("anagraficaModel", new AnagraficaPersonaleVVFModel(null, "", "", "", null, "", "", "", "", "", "", ""));
	    model.addAttribute("anagrafica", AnagraficaPersonaleVVFDTO.findAll());
	    //List<AnagraficaPersonaleVVFModel> a = repo.findAll();
	    //model.addAttribute("anagrafica", a);
	    return "anagraficavvf";
	  }

	  //Save Anagrafica
	  @PostMapping("/anagraficavvf")
	  public String saveAnagrafica(@ModelAttribute AnagraficaPersonaleVVFModel anagraficaModel, BindingResult bindingResult, Model model) throws SocketTimeoutException {
	    System.out.println("salvataggio A DB in corso....");
	    System.out.println(anagraficaModel == null);
	    Boolean esito = AnagraficaPersonaleVVFDTO.insertPerson(anagraficaModel);
	    if (esito) {
		    model.addAttribute("msgEsito","Inserimento effettuato con successo!");
		}else {model.addAttribute("msgEsito","Errore nel salvataggio!");}
	    model.addAttribute("esito",esito.toString());

	    //Init page redirect
	    model.addAttribute("menu", Menu.menu("anagraficavvf"));
	    model.addAttribute("anagraficaModel", new AnagraficaPersonaleVVFModel(null, "", "", "", null, "", "", "", "", "", "", ""));
	    model.addAttribute("anagrafica", AnagraficaPersonaleVVFDTO.findAll());
	    return "anagraficavvf";
	    //return "redirect:/anagraficavvf";
	  }
	  
	  
	  @PostMapping("/anagraficavvf/remove/{id}")
	  public String removeAnagraficaById(@PathVariable String id, Model model) throws SocketTimeoutException {
	    System.out.println("rimozione A DB in corso....");
	    Boolean esito = AnagraficaPersonaleVVFDTO.deleteById(id) > 0? true: false;
	    if (esito) {
		    model.addAttribute("msgEsito","Rimozione persona effettuata con successo!");
		}else {model.addAttribute("msgEsito","Errore nella rimozione della persona!");}
	    model.addAttribute("esito",esito.toString());
	    
	    return anagraficaVVF(model);
	  }
	  
	  @PostMapping("/anagraficavvf/modify/{id}")
	  public String modifyAnagraficaById(@PathVariable String id, @RequestBody Document document, Model model) throws SocketTimeoutException {
	    Boolean esito = AnagraficaPersonaleVVFDTO.updateById(id, document) > 0? true: false;
	    if (esito) {
		    model.addAttribute("msgEsito","Modifica persona effettuata con successo!");
		} else {model.addAttribute("msgEsito","Errore nella modifica della persona!");}
	    model.addAttribute("esito", esito.toString());
	    
	    return anagraficaVVF(model);
	  }
	  
	  
	  
	  /* @GetMapping("/login")
	  public String loginForm(Model model) {
	    model.addAttribute("logincredentials", new LoginCredentials());
	    model.addAttribute("menu", Menu.menu());
	    System.out.println("login");
	    return "home";
	  }
	  
	  @PostMapping("/login")
	  public String loginSubmit(@ModelAttribute LoginCredentials logincredentials, Model model) {
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(12);
		data.add(19);
		data.add(3);
		data.add(5);
		data.add(2);
		data.add(3);
		
	    model.addAttribute("logincredentials", logincredentials);
	    model.addAttribute("data", data);
	    model.addAttribute("menu", Menu.menu());
	    System.out.println("login->home");
	    return "home";
	  }*/


}
