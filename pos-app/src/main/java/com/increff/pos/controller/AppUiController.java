package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView employee() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView admin() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("order.html");
	}
	
	@RequestMapping(value = "/ui/reports")
	public ModelAndView reports() {
		return mav("reports.html");
	}
	
	@RequestMapping(value = "/ui/inventoryReport")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}
	
	@RequestMapping(value = "/ui/salesReport")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}

}