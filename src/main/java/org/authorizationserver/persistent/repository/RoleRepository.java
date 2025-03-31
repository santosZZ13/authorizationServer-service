package org.authorizationserver.persistent.repository;

import org.authorizationserver.persistent.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {
	Optional<RoleEntity> findByName(String name);
}