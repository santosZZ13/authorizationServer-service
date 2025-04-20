package org.authorizationserver.dao.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.dao.VerificationTokenModelRepository;
import org.authorizationserver.mapper.VerificationTokenMapper;
import org.authorizationserver.model.VerificationTokenModel;
import org.authorizationserver.persistent.entity.VerificationTokenEntity;
import org.authorizationserver.persistent.repository.VerificationTokenRepository;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class VerificationTokenModelRepositoryImpl implements VerificationTokenModelRepository {

	private final VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationTokenModel getVerificationTokenModelByToken(String token) {
		VerificationTokenEntity verificationTokenRepositoryByToken = verificationTokenRepository.findByToken(token);
		return VerificationTokenMapper.toModel(verificationTokenRepositoryByToken);
	}

	@Override
	public void deleteByToken(String token) {
		VerificationTokenModel tokenModel = getVerificationTokenModelByToken(token);
		if (tokenModel != null) {
			verificationTokenRepository.deleteById(Long.valueOf(tokenModel.getId()));
		}
	}
}
