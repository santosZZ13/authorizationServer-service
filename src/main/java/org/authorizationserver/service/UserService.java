package org.authorizationserver.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.dto.UserDto;

public interface UserService {
	UserDto.Response getCurrentUser();
	void registerUser(RegisterDto.Request registerRequestDto,
					  HttpServletRequest request, HttpServletResponse response);
}
