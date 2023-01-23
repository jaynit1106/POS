package com.increff.pos.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.AboutAppData;
import com.increff.pos.service.AboutAppService;

@Component
public class AboutAppDto {

	@Autowired
	private final AboutAppService service = new AboutAppService();
	
	public AboutAppData getDetails() {
		AboutAppData d = new AboutAppData();
		d.setName(service.getName());
		d.setVersion(service.getVersion());
		return d;
	}
}
