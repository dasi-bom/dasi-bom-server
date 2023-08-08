package com.example.server.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.server.global.jwt.AccessTokenUtil;
import com.example.server.global.jwt.JwtAuthenticationFilter;
import com.example.server.global.jwt.handler.OAuth2AuthenticationFailureHandler;
import com.example.server.global.jwt.handler.OAuth2AuthenticationSuccessHandler;
import com.example.server.global.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final AccessTokenUtil accessTokenUtil;
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin().disable()
			.httpBasic().disable()
			.csrf().disable()
			.headers().frameOptions().disable()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests(authorizeRequests -> authorizeRequests
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/social/login").permitAll()
				.antMatchers("/member/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2Login -> oauth2Login
				.defaultSuccessUrl("/login-success")
				.loginPage("/social/login")
				.userInfoEndpoint(userInfoEndpoint ->
					userInfoEndpoint.userService(principalOauth2UserService)
				)
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler)
			)
			.addFilterBefore(new JwtAuthenticationFilter(accessTokenUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
