package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.service.AboutAppService;

public class AboutAppServiceTest extends AbstractUnitTest {

	@Autowired
	private AboutAppService service;

	@Test
	public void testServiceApis() {
		assertEquals("Pos Application", service.getName());
		assertEquals("1.0", service.getVersion());
	}

}
