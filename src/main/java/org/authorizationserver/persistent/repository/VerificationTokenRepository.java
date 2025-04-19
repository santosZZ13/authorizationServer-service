package org.authorizationserver.persistent.repository;

import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
	VerificationTokenEntity findByToken(String token);

	void deleteByUser(UserEntity user);
}
