package org.authorizationserver.configs.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Log4j2
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
														  CorsConfigurationSource corsConfigurationSource,
														  AuthenticationSuccessHandler authenticationSuccessHandler,
														  AuthenticationFailureHandler authenticationFailureHandler) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(
						(authorizeRequests) -> authorizeRequests
								.requestMatchers("/oauth2/**").permitAll()
								.requestMatchers("/main.css").permitAll()
								.requestMatchers("/login").permitAll()
								.requestMatchers("/signup").permitAll()
								.requestMatchers("/register").permitAll()
								.requestMatchers("/api/login").permitAll() // Cho phép truy cập endpoint login
								.requestMatchers("/client").permitAll()
								.anyRequest().authenticated())
				.formLogin(formLogin -> formLogin
								.loginPage("/login")
								.permitAll()
								.successHandler(authenticationSuccessHandler)
//						.failureHandler(authenticationFailureHandler)
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
				)
				.logout(LogoutConfigurer::permitAll)
				// Khi bạn thêm .oauth2Login(withDefaults()), Spring Security tự động tạo một endpoint /oauth2/authorization/{registrationId}
				// (ví dụ: /oauth2/authorization/google) để khởi động flow OAuth2.
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login") // Chỉ định trang login tùy chỉnh làm trang bắt đầu
						.successHandler(authenticationSuccessHandler)
						.failureHandler((request, response, exception) -> {
							log.info("Google login failed: {}", exception.getMessage());
							response.sendRedirect("/login?error=google");
						})
						.permitAll()
				)
				.addFilterBefore(new GenericFilter() {
					@Override
					public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
							throws IOException, ServletException {
						HttpServletRequest req = (HttpServletRequest) request;
						String path = req.getServletPath();
						String grantType = req.getParameter("grant_type");
						if ("/oauth2/token".equals(path) && grantType != null) {
							log.info("Processing token request with grant_type: {}", grantType);
							if ("grant_password".equals(grantType)) {
								log.info("Using custom grant_password flow");
							} else if ("authorization_code".equals(grantType)) {
								log.info("Using authorization_code flow");
							}
						} else if ("/oauth2/authorize".equals(path)) {
							log.info("Processing authorization_code flow (authorize endpoint)");
						}
						chain.doFilter(request, response);
					}
				}, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
