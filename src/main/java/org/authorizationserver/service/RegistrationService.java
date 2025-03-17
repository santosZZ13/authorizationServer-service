package org.authorizationserver.service;

import org.authorizationserver.dto.RegisterDto;
import org.springframework.ui.Model;

public interface RegistrationService {
	RegisterDto.Response register(RegisterDto.Request registerDto, Model model);
}
