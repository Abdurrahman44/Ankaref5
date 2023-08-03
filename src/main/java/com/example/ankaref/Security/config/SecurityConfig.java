package com.example.ankaref.Security.config;

import com.example.ankaref.Security.JwtAuthenticationEntryPoint;
import com.example.ankaref.Security.JwtAuthenticationFilter;
import com.example.ankaref.Security.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@Configuration
@EnableWebSecurity
@NoArgsConstructor
@AllArgsConstructor
public class SecurityConfig   {
	@Autowired
	private UserDetailsServiceImpl userDetailsImp;
	@Autowired
	private JwtAuthenticationEntryPoint handler;
	@Bean
	public JwtAuthenticationFilter jwtauthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

		return authenticationConfiguration.getAuthenticationManager();

	}@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	@Autowired
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
        .userDetailsService(userDetailsImp)
        .passwordEncoder(passwordEncoder());
    }
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		CorsConfiguration config=new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("Options");
		config.addAllowedMethod("Head");
		config.addAllowedMethod("Get");
		config.addAllowedMethod("Put");
		config.addAllowedMethod("Post");
		config.addAllowedMethod("Delete");
		config.addAllowedMethod("Patch");
		source.registerCorsConfiguration("/**", config);
		
		
		return new CorsFilter(source);
	}
	@Bean
	public SecurityFilterChain  filterChain(HttpSecurity httpSecurity)throws Exception{
		
		 httpSecurity.authorizeHttpRequests().anyRequest().authenticated()
		 .and().httpBasic().and().exceptionHandling().authenticationEntryPoint(handler)
		 .and()
		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				 .and()

		 .addFilterBefore(jwtauthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		 
	    return httpSecurity.build();
    }
}