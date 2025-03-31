package org.authorizationserver.dao;

import org.authorizationserver.model.RoleModel;

public interface RoleDaoRepository {
	RoleModel getByName(String name);
	RoleModel getDefaultRole();
}
