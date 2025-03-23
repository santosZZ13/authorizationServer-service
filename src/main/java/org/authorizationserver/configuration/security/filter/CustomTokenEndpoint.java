package org.authorizationserver.configuration.security.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;


@Component
public class CustomTokenEndpoint {

	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<?> tokenGenerator;

	public CustomTokenEndpoint(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<?> tokenGenerator) {
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
	}

	public void handleTokenResponse(HttpServletResponse response, Authentication authentication) throws Exception {
		OAuth2AccessTokenAuthenticationToken tokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		// Lấy token từ authentication
		String accessToken = tokenAuthentication.getAccessToken().getTokenValue();
		String refreshToken = tokenAuthentication.getRefreshToken() != null ? tokenAuthentication.getRefreshToken().getTokenValue() : null;

		// Lưu access_token vào cookie
		if (accessToken != null) {
			Cookie accessTokenCookie = new Cookie("access_token", accessToken);
			accessTokenCookie.setHttpOnly(true);
			accessTokenCookie.setSecure(true); // Chỉ gửi qua HTTPS (bỏ qua nếu đang phát triển local)
			accessTokenCookie.setPath("/");
			accessTokenCookie.setMaxAge(15 * 60); // Hết hạn sau 15 phút
			response.addCookie(accessTokenCookie);
		}

		// Lưu refresh_token vào cookie
		if (refreshToken != null) {
			Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
			refreshTokenCookie.setHttpOnly(true);
			refreshTokenCookie.setSecure(true);
			refreshTokenCookie.setPath("/");
			refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Hết hạn sau 30 ngày
			response.addCookie(refreshTokenCookie);
		}

		// Ghi response với thông báo thành công (không chứa token)
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("{\"status\": \"success\", \"message\": \"Token set in cookies\"}");
		response.getWriter().flush();
	}
}