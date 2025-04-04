package org.authorizationserver.enums;

public enum Provider {
	GOOGLE("google"),
	GITHUB("github"),
	LOCAL("local"),
	APPLE("apple"),
	UNKNOWN("unknown");
	private final String provider;

	Provider(String provider) {
		this.provider = provider;
	}
}