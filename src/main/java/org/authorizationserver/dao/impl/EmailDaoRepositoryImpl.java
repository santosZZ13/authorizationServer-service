package org.authorizationserver.dao.impl;

import org.authorizationserver.dao.EmailDaoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EmailDaoRepositoryImpl implements EmailDaoRepository {

	@Override
	public void sendVerificationEmail(String email, String otp) {
		System.out.println("Sending OTP " + otp + " to " + email);
	}
}
