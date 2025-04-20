package org.authorizationserver.service;

public interface VerifyUserService {

	void verifyCode(String email, String code);

	void sendVerificationCode(String email);

}
