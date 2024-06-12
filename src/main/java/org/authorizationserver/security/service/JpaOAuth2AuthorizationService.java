package org.authorizationserver.security.service;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaOAuth2AuthorizationService extends JdbcOAuth2AuthorizationService {
	public JpaOAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
		super(jdbcOperations, registeredClientRepository);
	}


	@Override
	public void save(OAuth2Authorization authorization) {
		super.save(authorization);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		super.remove(authorization);
	}

	@Override
	public OAuth2Authorization findById(String id) {
		return super.findById(id);
	}

	@Override
	public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
		return super.findByToken(token, tokenType);
	}
}
