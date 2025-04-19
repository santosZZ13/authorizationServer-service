package org.authorizationserver.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.dto.UserInfoDto;

public interface UserService {
	UserInfoDto.Response getCurrentUser();

	void registerUser(RegisterDto.Request registerRequestDto,
					  HttpServletRequest request, HttpServletResponse response);

	void verifyCode(String email, String code);
}
