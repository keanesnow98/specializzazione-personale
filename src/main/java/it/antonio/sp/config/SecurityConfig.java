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
import it.antonio.sp.util.Constants;

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
				.antMatchers("/javax.faces.resource/**/**.xhtml").permitAll()
				.antMatchers("/specializzazionevvf/**.xhtml").permitAll()
				.antMatchers("/specializzazionevvf/auth/**.xhtml").permitAll()
				.antMatchers("/specializzazionevvf/api/admin/").hasRole("ADMIN")
				.antMatchers("/specializzazionevvf/api/admin/specialties.xhtml").hasAuthority(Constants.ROLE_ADMIN_SPECIALTIES_ALL)
				.antMatchers("/specializzazionevvf/api/admin/qualifications.xhtml").hasAuthority(Constants.ROLE_ADMIN_QUALIFICATIONS_ALL)
				.antMatchers("/specializzazionevvf/api/admin/users.xhtml").hasAuthority(Constants.ROLE_ADMIN_USER_MANAGEMENT_ALL)
				.antMatchers("/specializzazionevvf/api/dashboard.xhtml").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER_DASHBOARD_ALL)
				.antMatchers("/specializzazionevvf/api/anagraphic.xhtml").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER_DASHBOARD_ALL)
				.antMatchers("/specializzazionevvf/api/reports-by-turno.xhtml").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER_REPORTS_BY_TURNO_ALL)
				.antMatchers("/specializzazionevvf/api/reports-by-specialty.xhtml").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER_REPORTS_BY_SPECIALTY_ALL)
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.usernameParameter("email")
				.loginPage("/specializzazionevvf/auth/login.xhtml").permitAll()
				.failureUrl("/specializzazionevvf/auth/login.xhtml?error=true")
				.defaultSuccessUrl("/specializzazionevvf/test.xhtml")
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
	    web.ignoring().antMatchers("/specializzazionevvf/auth/register.xhtml", "/specializzazionevvf/test.xhtml");
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
