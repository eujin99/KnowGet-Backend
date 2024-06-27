package com.knowget.knowgetbackend.global.config.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService;

	private final String[] publicURLs = {"/api/v1/user/**", "/api/v1/posts/**", "/api/v1/education/**",
		"/api/v1/job-guide", "/api/v1/job-guide/{id}", "/api/v1/success-case", "/api/v1/success-case/{caseId}",
		"/api/v1/success-case/search", "/api/v1/success-case/{caseId}/comments",
		"/api/v1/comment/{commentId}/replies", "/api/v1/admin/register", "/api/v1/admin/login"};
	private final String[] protectedUrls = {"/api/v1/notification/**", "/api/v1/bookmark/**",
		"/api/v1/success-case/{caseId}/comment",
		"/api/v1/success-case/{caseId}/comment/{commentId}", "/api/v1/comment/{commentId}/reply",
		"/api/v1/comment/{commentId}/reply/{replyId}", "/api/v1/counseling/**"};

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://d26s7gklfb7nph.cloudfront.net"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setMaxAge(3600L);
				return config;
			}));
		http
			.csrf(CsrfConfigurer<HttpSecurity>::disable);
		http
			.formLogin(AbstractHttpConfigurer::disable)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint((request, response, authException) -> response.setStatus(401))
				.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403)));
		http
			.httpBasic(AbstractHttpConfigurer::disable);
		http
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http
			.securityContext((context) -> context.requireExplicitSave(false));
		http
			.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
		http
			.authorizeHttpRequests(requests ->
				requests
					.requestMatchers(publicURLs).permitAll()
					.requestMatchers(protectedUrls).authenticated()
					.requestMatchers("/api/v1/admin/users", "/api/v1/admin/user/{userId}", "/api/v1/image/**",
						"/api/v1/document/**").hasRole("ADMIN")
					.requestMatchers("/api/v1/mypage/**").hasRole("USER")
					.anyRequest().authenticated()
			);

		return http.build();
	}

}