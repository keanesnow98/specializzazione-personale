package it.antonio.sp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import it.antonio.sp.util.Constants;

@Controller
public class MainController {
	@GetMapping({ "/", Constants.URI_SPECIALIZZAZIONEVVF })
	public String homePage(HttpServletRequest req) {
		return "redirect:" + Constants.URI_SPECIALIZZAZIONEVVF + Constants.URI_API + Constants.URI_DASHBOARD;
	}
}
