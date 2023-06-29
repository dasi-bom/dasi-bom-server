package com.example.server.config;

import com.example.server.config.jwt.AccessTokenUtil;
import com.example.server.config.jwt.JwtAuthenticationFilter;
import com.example.server.config.jwt.OAuth2AuthenticationSuccessHandler;
import com.example.server.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final AccessTokenUtil accessTokenUtil;
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
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

		http.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/social/login").permitAll()
				.anyRequest().authenticated()

				.and()
				.oauth2Login()
				.loginPage("/social/login")
				.userInfoEndpoint()
				.userService(principalOauth2UserService)
				.and()
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(accessTokenUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
