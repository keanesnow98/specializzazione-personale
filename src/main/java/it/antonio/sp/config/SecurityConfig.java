package it.antonio.sp.config;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import it.antonio.sp.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	UserService userService;
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) {
		try {
			http.csrf().disable();
			http
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/**.xhtml").permitAll()
				.antMatchers("/javax.faces.resource/**").permitAll()
				.antMatchers("/specializzazionevvf/auth/**.xhtml").permitAll()
				.antMatchers("/specializzazionevvf/api/admin/**.xhtml").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.usernameParameter("email")
				.loginPage("/specializzazionevvf/auth/login.xhtml").permitAll()
				.failureUrl("/specializzazionevvf/auth/login.xhtml?error=true")
				.defaultSuccessUrl("/specializzazionevvf/api/helloworld.xhtml")
				.and()
				.logout()
				.logoutSuccessUrl("/specializzazionevvf/auth/login.xhtml");
			return http.build();
		}
		catch (Exception ex) {
			throw new BeanCreationException("Wrong spring security configuration", ex);
		}
	}
	
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/specializzazionevvf/auth/register.xhtml");
	}


	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		userService.findAll().forEach(user -> {
			manager.createUser(User.builder()
				.username(user.getEmail())
				.password("{bcrypt}" + user.getPassword())
				.authorities(user.getRoles().toArray(new String[0])).build());
		});
		
		return manager;
	}
}
