package org.authorizationserver.mapper;

import org.authorizationserver.model.RoleModel;
import org.authorizationserver.persistent.entity.RoleEntity;

import java.util.stream.Collectors;

public final class RoleMapper {

	public static RoleModel toRoleModel(RoleEntity roleEntity) {
		if (roleEntity == null) {
			return null;
		}

		return RoleModel.builder()
				.name(roleEntity.getName())
				.authorities(
						roleEntity.getAuthorities().stream()
								.map(AuthorityMapper::toModel)
								.collect(Collectors.toSet())
				)
				.build();
	}
}
