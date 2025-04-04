package org.authorizationserver.persistent.repository;

import org.authorizationserver.model.RoleModel;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

	public static final String DEFAULT_ROLE = "USER";

	Optional<RoleEntity> findByName(String name);

	default RoleEntity getDefaultRoleEntity() {
		return findByName(DEFAULT_ROLE)
				.orElseThrow(() -> new IllegalStateException("Default role not found"));
	}
}