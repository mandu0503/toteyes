package com.kt.totaleyes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.kt.totaleyes.security.handler.LoginFailureHandler;
import com.kt.totaleyes.security.handler.LoginSucessHandler;
import com.kt.totaleyes.security.provider.AuthProvider;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthProvider authProvider;
	
	@Autowired
	private LoginFailureHandler loginFailureHandler;
				
	@Override
	public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**"); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    http.authorizeRequests()
	   	    .antMatchers("/login").permitAll()
	   	    .antMatchers("/logout").permitAll()
	        .antMatchers("/**").hasRole("USER, ADMIN")
	        //.antMatchers("/admin/**").access("ROLE_ADMIN")
	        .antMatchers("/**").authenticated();
	    
        http.formLogin()
	        .loginPage("/login")
	        .loginProcessingUrl("/authenticate")	        
	        .failureHandler(loginFailureHandler)
	        .successHandler(loginSucessHandler())
	        .usernameParameter("userId")
	        .passwordParameter("password");
        
        http.logout()
	        .logoutUrl("/logout") // default
	        .logoutSuccessUrl("/login")
	        .invalidateHttpSession(true)
	        .permitAll();
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }
    
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	@Bean
    public AuthenticationSuccessHandler loginSucessHandler() {
      return new LoginSucessHandler("/main");
    }
}
