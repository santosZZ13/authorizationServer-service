//package org.authorizationserver.configuration.security.filter;
//
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.log4j.Log4j2;
//import org.authorizationserver.configuration.security.model.HttpServletRspWrapper;
//import org.springframework.http.MediaType;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Log4j2
//public class CustomTokenResponseFilter extends OncePerRequestFilter {
//
//	private final OAuth2AuthorizationService authorizationService;
//
//	public CustomTokenResponseFilter(OAuth2AuthorizationService authorizationService) {
//		this.authorizationService = authorizationService;
//	}
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		String requestPath = request.getServletPath();
//
//		// Chỉ xử lý cho endpoint /oauth2/token
//
//		if (!"/oauth2/token".equals(requestPath)) {
//			filterChain.doFilter(request, response);
//			return;
//		}
//
//		// Tạo một response wrapper để capture body
//		HttpServletRspWrapper responseWrapper = new HttpServletRspWrapper(response);
//		filterChain.doFilter(request, responseWrapper);
//
//		// Lấy body từ response (chứa access_token, refresh_token, v.v.)
//		String responseBody = responseWrapper.getCapturedBody();
//		log.info("Original token response body: {}", responseBody);
//
//		// Parse body để lấy token (giả sử body là JSON)
//		// Trong thực tế, bạn có thể dùng ObjectMapper để parse JSON
//		// Ở đây mình giả lập parse thủ công để minh họa
//		Map<String, String> tokenResponse = parseTokenResponse(responseBody);
//		String accessToken = tokenResponse.get("access_token");
//		String refreshToken = tokenResponse.get("refresh_token");
//
//		if (accessToken != null) {
//			// Lưu access_token vào cookie
//			Cookie accessTokenCookie = new Cookie("access_token", accessToken);
//			accessTokenCookie.setHttpOnly(true);
//			accessTokenCookie.setSecure(true); // Chỉ gửi qua HTTPS (bỏ qua nếu đang phát triển local)
//			accessTokenCookie.setPath("/");
//			accessTokenCookie.setMaxAge(15 * 60); // Hết hạn sau 15 phút
//			response.addCookie(accessTokenCookie);
//		}
//
//		if (refreshToken != null) {
//			// Lưu refresh_token vào cookie
//			Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
//			refreshTokenCookie.setHttpOnly(true);
//			refreshTokenCookie.setSecure(true);
//			refreshTokenCookie.setPath("/");
//			refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60); // Hết hạn sau 30 ngày
//			response.addCookie(refreshTokenCookie);
//		}
//
//		// Ghi lại response với thông báo thành công (không chứa token)
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.getWriter().write("{\"status\": \"success\", \"message\": \"Token set in cookies\"}");
//		response.getWriter().flush();
//	}
//
//	// Giả lập parse JSON (trong thực tế, bạn nên dùng ObjectMapper)
//	private Map<String, String> parseTokenResponse(String responseBody) {
//		Map<String, String> result = new HashMap<>();
//		// Ví dụ: responseBody = {"access_token":"abc", "refresh_token":"xyz", ...}
//		String[] pairs = responseBody.replace("{", "").replace("}", "").split(",");
//		for (String pair : pairs) {
//			String[] keyValue = pair.split(":");
//			if (keyValue.length == 2) {
//				String key = keyValue[0].replace("\"", "").trim();
//				String value = keyValue[1].replace("\"", "").trim();
//				result.put(key, value);
//			}
//		}
//		return result;
//	}
//}