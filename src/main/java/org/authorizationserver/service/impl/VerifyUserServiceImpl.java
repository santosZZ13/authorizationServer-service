package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.dao.UserModelRepository;
import org.authorizationserver.dao.VerificationTokenModelRepository;
import org.authorizationserver.exception.CannotConfirmCodeException;
import org.authorizationserver.exception.ExpiredCodeException;
import org.authorizationserver.exception.NotFoundUserException;
import org.authorizationserver.exception.RegistrationException;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.model.VerificationTokenModel;
import org.authorizationserver.service.VerifyUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class VerifyUserServiceImpl implements VerifyUserService {

	private final UserModelRepository userModelRepository;
	private final VerificationTokenModelRepository verificationTokenModelRepository;

	@Override
	public void verifyCode(String email, String code) {
		UserModel userModel = userModelRepository.findByEmail(email);
		if (Objects.isNull(userModel)) {
			throw new NotFoundUserException("User not found", "", "");
		}

		VerificationTokenModel verificationTokenModel = verificationTokenModelRepository.getVerificationTokenModelByToken(code);
		if (verificationTokenModel == null) {
			throw new CannotConfirmCodeException("Invalid verification code", "", "");
		}

		boolean isVerified = Objects.equals(userModel.getId().toString(), verificationTokenModel.getUserId());
		if (!isVerified) {
			throw new CannotConfirmCodeException("Invalid verification code", "", "");
		}

		boolean isExpiredCode = verificationTokenModel.getVerificationExpiry().isBefore(LocalDateTime.now());
		if (isExpiredCode) {
			throw new ExpiredCodeException("Verification code has expired", "", "");
		}

		userModelRepository.updateUserActiveStatus(userModel.getEmail(), true);
//		verificationTokenModelRepository.deleteByToken(code);
	}

	@Override
	public void sendVerificationCode(String email) {

	}
}
