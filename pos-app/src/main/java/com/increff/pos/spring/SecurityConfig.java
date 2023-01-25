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
				.antMatchers(HttpMethod.GET, "/api/brands/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/brands/**").hasAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/products/**").hasAuthority("supervisor")//
				.antMatchers(HttpMethod.GET, "/api/inventorys/**").hasAnyAuthority("operator","supervisor")//
				.antMatchers("/api/inventorys/**").hasAuthority("supervisor")//
				.antMatchers("/api/reports/**").hasAuthority("supervisor")//
				.antMatchers("/api/scheduler/**").hasAuthority("supervisor")//
				.antMatchers("/api/orders/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/api/orderitems/**").hasAnyAuthority("supervisor","operator")//
				// UI Pages
				.antMatchers("/ui/brands/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/products/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/inventorys/**").hasAnyAuthority("supervisor","operator")//
				.antMatchers("/ui/reports/**").hasAuthority("supervisor")//
				.antMatchers("/ui/inventorysReport/**").hasAuthority("supervisor")//
				.antMatchers("/ui/salesReport/**").hasAuthority("supervisor")//
				.antMatchers("/ui/orders/**").hasAnyAuthority("supervisor","operator")//
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
