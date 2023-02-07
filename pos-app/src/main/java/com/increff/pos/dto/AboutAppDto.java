package com.increff.pos.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.AboutAppData;
import com.increff.pos.service.AboutAppService;

@Component
public class AboutAppDto {

	@Autowired
	private AboutAppService service;
	
	public AboutAppData getDetails() {
		AboutAppData d = new AboutAppData();
		d.setName(service.getName());
		d.setVersion(service.getVersion());
		return d;
	}
}
