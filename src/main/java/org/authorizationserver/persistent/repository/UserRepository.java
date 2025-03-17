package org.authorizationserver.persistent.repository;

import org.authorizationserver.persistent.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email);
}