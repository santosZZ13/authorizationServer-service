package org.authorizationserver.mapper;

import org.authorizationserver.model.AuthorityModel;
import org.authorizationserver.persistent.entity.AuthorityEntity;

public final class AuthorityMapper {
	private AuthorityMapper() {
		// Prevent instantiation
	}

	public static AuthorityModel toModel(AuthorityEntity entity) {
		if (entity == null) {
			return null;
		}

		return AuthorityModel.builder()
				.id(entity.getId())
				.name(entity.getName())
				.build();
	}
}
