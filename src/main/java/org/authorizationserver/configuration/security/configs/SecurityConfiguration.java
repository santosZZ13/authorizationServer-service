package org.authorizationserver.configuration.security.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.authorizationserver.configuration.security.handler.SocialLoginAuthenticationSuccessHandler;
import org.authorizationserver.configuration.security.service.UserServiceOAuth2UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Log4j2
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
														  AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
//								.loginProcessingUrl("/api/login") // Endpoint xử lý login
//								.usernameParameter("email")
//								.passwordParameter("password")
//								.successHandler((request, response, authentication) -> {
//									// Trả về response JSON khi login thành công
////									response.setStatus(HttpServletResponse.SC_OK);
////									response.setContentType("application/json");
////									response.getWriter().write("{\"status\": \"success\", \"message\": \"Login successful\"}");
////									response.sendRedirect("http://localhost:3000");/**/
//									// process the default success handler
////									 defaultSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//									// process the custom success handler
////									authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//								})
//								.failureHandler((request, response, exception) -> {
//									response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//									response.setContentType("application/json");
//									response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid email or password\"}");
//								})
				)
//				.logout(logout -> logout
//						.logoutUrl("/logout")
//						.invalidateHttpSession(true)
//						.deleteCookies("JSESSIONID")
//						.permitAll()
//				)
				.logout(LogoutConfigurer::permitAll)
				// Khi bạn thêm .oauth2Login(withDefaults()), Spring Security tự động tạo một endpoint /oauth2/authorization/{registrationId}
				// (ví dụ: /oauth2/authorization/google) để khởi động flow OAuth2.
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login") // Chỉ định trang login tùy chỉnh làm trang bắt đầu
						.successHandler(authenticationSuccessHandler)
						.failureHandler((request, response, exception) -> {
							log.info("Google login failed: {}", exception.getMessage());
							response.sendRedirect("/login?error=google"); // Chuyển hướng khi thất bại
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

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler(UserServiceOAuth2UserHandler handler) {
		SocialLoginAuthenticationSuccessHandler authenticationSuccessHandler = new SocialLoginAuthenticationSuccessHandler();
		authenticationSuccessHandler.setOidcUserHandler(handler);
		return authenticationSuccessHandler;
	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user = User.builder()
//				.username("admin")
//				// {noop} means "no operation," i.e., a raw password without any encoding applied.
//				.password("{noop}secret")
//				.roles("ADMIN")
//				.authorities("ARTICLE_READ", "ARTICLE_WRITE")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}

	private CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration ccfg = new CorsConfiguration();
			ccfg.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Chỉ cho phép origin này
			ccfg.setAllowedMethods(Collections.singletonList("*")); // Cho phép tất cả phương thức (GET, POST, v.v.)
			ccfg.setAllowCredentials(true); // Cho phép gửi cookie/credentials
			ccfg.setAllowedHeaders(Collections.singletonList("*")); // Cho phép tất cả header
			ccfg.setExposedHeaders(Arrays.asList("Authorization")); // Phơi bày header Authorization nếu cần
			ccfg.setMaxAge(3600L); // Cache preflight request trong 1 giờ
			return ccfg;
		};
	}

//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//		config.setAllowedMethods(Collections.singletonList("*"));
//		config.setAllowCredentials(true);
//		config.setAllowedHeaders(Collections.singletonList("*"));
//		config.setExposedHeaders(Arrays.asList("Authorization"));
//		config.setMaxAge(3600L);
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
//	}
}
