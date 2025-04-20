package org.authorizationserver.mapper;

import org.authorizationserver.model.VerificationTokenModel;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.entity.VerificationTokenEntity;

public final class VerificationTokenMapper {

	private VerificationTokenMapper() {
		// Prevent instantiation
	}

	public static VerificationTokenModel toModel(VerificationTokenEntity entity) {
		if (entity == null) {
			return null;
		}
		return VerificationTokenModel.builder()
				.id(entity.getId())
				.token(entity.getToken())
				.userId(entity.getUser().getId().toString())
				.verificationExpiry(entity.getVerificationExpiry())
				.build();
	}


	public static VerificationTokenEntity toEntity(VerificationTokenModel model, UserEntity userEntity) {
		if (model == null) {
			return null;
		}
		return VerificationTokenEntity.builder()
//				.id(model.getId() != null ? model.getId().longValue() : null)
				.token(model.getToken())
				.user(userEntity)
				.verificationExpiry(model.getVerificationExpiry())
				.build();
	}
}
