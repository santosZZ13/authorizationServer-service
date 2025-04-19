package org.authorizationserver.util;

import java.util.Random;

public class GenerateCodeOTP {

	public static String generateOTP() {
		Random random = new Random();
		return String.format("%04d", random.nextInt(10000)); // Tạo mã 4 chữ số
	}
}
