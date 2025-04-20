package org.authorizationserver.constants;

public class SecurityConstants {
	// Public endpoints
	public static final String[] PUBLIC_ENDPOINTS = {
			"/oauth2/**",
			"/main.css",
			"/login",
			"/signup",
			"/days-in-month",
			"/api/login",
			"/client",
			"/confirmCode",
			"/error",
			"/internalError"
	};

	// Authentication endpoints
	public static final String[] AUTHENTICATION_ENDPOINTS = {
			"/",
	};


	private SecurityConstants() {
		// Ngăn khởi tạo instance
	}
}
