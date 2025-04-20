package org.authorizationserver.dao;

import org.authorizationserver.model.VerificationTokenModel;

public interface VerificationTokenModelRepository {
	VerificationTokenModel getVerificationTokenModelByToken(String token);

	void deleteByToken(String token);
}
