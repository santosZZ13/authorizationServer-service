package org.authorizationserver.dao;

import lombok.AllArgsConstructor;
import org.authorizationserver.model.RoleModel;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.repository.RoleRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@AllArgsConstructor
public class RoleDaoRepositoryImpl implements RoleDaoRepository {

	public static final String DEFAULT_ROLE = "USER";


	private final RoleRepository roleRepository;


	@Override
	public RoleModel getByName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}

		RoleEntity roleEntity = roleRepository
				.findByName(name)
				.orElse(null);
		if (roleEntity != null) {
			return RoleModel.fromEntity(roleEntity);
		}
		return null;
	}

	@Override
	public RoleModel getDefaultRole() {
		return getByName(DEFAULT_ROLE);
	}
}
