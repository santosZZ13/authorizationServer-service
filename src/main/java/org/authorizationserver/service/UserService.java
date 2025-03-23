package org.authorizationserver.service;

import org.authorizationserver.dto.UserDto;

public interface UserService {
	UserDto.Response getCurrentUser();
}
