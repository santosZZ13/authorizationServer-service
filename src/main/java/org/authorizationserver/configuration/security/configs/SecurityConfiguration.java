package org.authorizationserver.configuration.security.configs;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * For basic security settings
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Log4j2
public class SecurityConfiguration {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private AuthenticationSuccessHandler authenticationSuccessHandler = this::sendAuthorizationResponse;


	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(
						(authorizeRequests) -> authorizeRequests
								.requestMatchers("/oauth2/**").permitAll()
								.requestMatchers("/main.css").permitAll()
								.requestMatchers("/index").permitAll()
								.requestMatchers("/login").permitAll()
								.requestMatchers("/api/login").permitAll() // Cho phép truy cập endpoint login
								.requestMatchers(HttpMethod.GET, "/foo").permitAll()
								.requestMatchers(HttpMethod.POST, "/foo").permitAll()
								.requestMatchers("/client").permitAll()
								.anyRequest().authenticated())
				.formLogin(withDefaults())
				.formLogin(formLogin ->
						formLogin
								.loginPage("/login") // Đặt lại để chỉ định URL login tùy chỉnh (nếu bạn muốn redirect đến frontend)
								.permitAll()
//								.loginProcessingUrl("/api/login") // Endpoint xử lý login
//								.usernameParameter("email") // Sử dụng email làm username
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
//									authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//								})
////								.failureHandler((request, response, exception) -> {
//									// Trả về response JSON khi login thất bại
//									response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//									response.setContentType("application/json");
//									response.getWriter().write("{\"status\": \"error\", \"message\": \"Invalid email or password\"}");
//								})
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
//				.oauth2Login(oauth ->
//								oauth
//										.successHandler(authenticationSuccessHandler)
//								.failureHandler((request, response, exception) -> response.sendRedirect("/login?error"))
//				.logout(LogoutConfigurer::permitAll)
				.build();
	}

//	@Bean
//	public AuthenticationSuccessHandler authenticationSuccessHandler(UserServiceOAuth2UserHandler handler) {
//		SocialLoginAuthenticationSuccessHandler authenticationSuccessHandler = new SocialLoginAuthenticationSuccessHandler();
//		authenticationSuccessHandler.setOidcUserHandler(handler);
//		return authenticationSuccessHandler;
//	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.builder()
				.username("admin")
				// {noop} means "no operation," i.e., a raw password without any encoding applied.
				.password("{noop}secret")
				.roles("ADMIN")
				.authorities("ARTICLE_READ", "ARTICLE_WRITE")
				.build();

		return new InMemoryUserDetailsManager(user);
	}

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

	private void sendAuthorizationResponse(HttpServletRequest request, HttpServletResponse response,
										   Authentication authentication) throws IOException {

		OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
				(OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(authorizationCodeRequestAuthentication.getRedirectUri()).queryParam(OAuth2ParameterNames.CODE, authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue());
		if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
			uriBuilder.queryParam(
					OAuth2ParameterNames.STATE,
					UriUtils.encode(authorizationCodeRequestAuthentication.getState(), StandardCharsets.UTF_8));
		}
		String redirectUri = uriBuilder.build(true).toUriString();		// build(true) -> Components are explicitly encoded
		this.redirectStrategy.sendRedirect(request, response, redirectUri);
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
