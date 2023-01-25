package com.increff.pos.controller;

import com.increff.pos.model.InfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
public class SiteUiController extends AbstractUiController {
	@Autowired
	private InfoData info;

	// WEBSITE PAGES
	@RequestMapping(value = "")
	public ModelAndView index() {
		return new ModelAndView("redirect:/site/login");
	}

	@RequestMapping(value = "/access/denied")
	public ModelAndView accessDenied(){
		return new ModelAndView("accessDenied.html");
	}

	@RequestMapping(value = "/site/login")
	public ModelAndView login() {

		return mav("login.html");
	}


	@RequestMapping(value = "/site/signup")
	public ModelAndView signup() {
		if(Objects.equals(info.getMessage(),"Invalid username or password"))info.setMessage("");
		return mav("signup.html");}

	@RequestMapping(value = "/site/logout")
	public ModelAndView logout() {
		return mav("login.html");
	}

}
