package org.authorizationserver.dao;

import org.springframework.stereotype.Repository;

@Repository
public class EmailDaoRepositoryImpl implements EmailDaoRepository {

	@Override
	public void sendVerificationEmail(String email, String otp) {
		System.out.println("Sending OTP " + otp + " to " + email);
	}
}
