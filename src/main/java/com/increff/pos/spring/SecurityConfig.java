package com.increff.pos.spring;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
			// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				.antMatchers("/api/brand").hasAuthority("supervisor")//
				.antMatchers("/api/product").hasAuthority("supervisor")//
				.antMatchers("/api/inventory").hasAuthority("supervisor")//
				.antMatchers("/api/report").hasAuthority("supervisor")//
				.antMatchers("/api/scheduler").hasAuthority("supervisor")//
				.antMatchers("/api/order").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/api/orderitem").hasAnyAuthority("supervisor","operator")//
				// UI Pages
				.antMatchers("/ui/brand").hasAuthority("supervisor")//
				.antMatchers("/ui/product").hasAuthority("supervisor")//
				.antMatchers("/ui/inventory").hasAuthority("supervisor")//
				.antMatchers("/ui/reports").hasAuthority("supervisor")//
				.antMatchers("/ui/inventoryReport").hasAuthority("supervisor")//
				.antMatchers("/ui/salesReport").hasAuthority("supervisor")//
				.antMatchers("/ui/order").hasAnyAuthority("supervisor","operator")//
				// Ignore CSRF and CORS
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

}
