package org.authorizationserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.persistent.entity.Role;
import org.authorizationserver.persistent.repository.RoleRepository;
import org.authorizationserver.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	public static final String DEFAULT_ROLE = "USER";

	private final RoleRepository roleRepository;

	@Override
	public Role getByName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}

		return roleRepository.findByName(name).orElse(null);
	}

	@Override
	public Role getDefaultRole() {
		return getByName(DEFAULT_ROLE);
	}
}
