package org.authorizationserver.service;


import org.authorizationserver.persistent.entity.Role;

public interface RoleService {
	Role getByName(String name);
	Role getDefaultRole();
}