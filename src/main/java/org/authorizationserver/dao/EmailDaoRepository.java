package org.authorizationserver.dao;

public interface EmailDaoRepository {
	void sendVerificationEmail(String email, String otp);
}
