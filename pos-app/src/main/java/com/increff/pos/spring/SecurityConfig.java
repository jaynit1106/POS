package com.increff.pos.spring;

import com.increff.pos.controller.AccessDeniedController;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

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
				.antMatchers(HttpMethod.GET, "/api/brand/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/brand/**").hasAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/product/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/product/**").hasAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/inventory/**").hasAuthority("supervisor")//
				.antMatchers("/api/report/**").hasAuthority("supervisor")//
				.antMatchers("/api/scheduler/**").hasAuthority("supervisor")//
				.antMatchers("/api/order/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/api/orderitem/**").hasAnyAuthority("supervisor","operator")//
				// UI Pages
				.antMatchers("/ui/brand/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/product/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/inventory/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/reports/**").hasAuthority("supervisor")//
				.antMatchers("/ui/inventoryReport/**").hasAuthority("supervisor")//
				.antMatchers("/ui/salesReport/**").hasAuthority("supervisor")//
				.antMatchers("/ui/order/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/home/**").hasAnyAuthority("supervisor","operator")//
				// Ignore CSRF and CORS
				.and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new AccessDeniedController();
	}

}
