package ru.taustudio.duckview.control.screenshotcontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.taustudio.duckview.control.screenshotcontrol.entity.ScUser;
import ru.taustudio.duckview.control.screenshotcontrol.user.UserDetailsService;

import java.util.Map;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/","/start","/api").permitAll()
				.and()
				.authorizeRequests().antMatchers("/home").authenticated()
				.and()
				.formLogin().defaultSuccessUrl("/success")
				.and().logout().logoutSuccessUrl("/")
				.and().anonymous().principal(new ScUser())
				.and()
				.csrf().disable();
	}


	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new DelegatingPasswordEncoder("bcrypt", Map.of("bcrypt", new BCryptPasswordEncoder()));
	}

	@Autowired
	public void configure( AuthenticationManagerBuilder auth, UserDetailsService service ) throws Exception {
//		auth.inMemoryAuthentication()
//				.withUser( "admin" ).password( "{bcrypt}$2a$12$4NgTAMMvuxBzPjZS4sdlH.iJyA4Hc2s0oW0icM7ZbhWn/zUlvaqgC" ).roles( "ADMIN" );
		auth.userDetailsService(service);
	}
}
