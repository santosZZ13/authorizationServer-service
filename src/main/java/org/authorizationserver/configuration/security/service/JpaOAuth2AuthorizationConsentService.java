package org.authorizationserver.configuration.security.service;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

public class JpaOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {

	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {

	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		return null;
	}
}
