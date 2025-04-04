package org.authorizationserver.security.service;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.repository.UserRepository;
import org.authorizationserver.security.mapper.OidcUserMapper;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.model.UserModel;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

	private final Map<String, OidcUserMapper> mappers;
	private final UserRepository userRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		OidcIdToken idToken = userRequest.getIdToken();
		OAuth2AccessToken accessToken = userRequest.getAccessToken();
		Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();

		String registrationId = clientRegistration.getRegistrationId();
		Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for such registrationId");
		if (!mappers.containsKey(registrationId)) {
			throw new OAuth2AuthenticationException("No mapper defined for such registrationId");
		}

		OidcUserMapper mapper = mappers.get(registrationId);
		final String email = idToken.getEmail();
		UserEntity userEntity = getUserEntity(email);

		if (userEntity != null) {
			return mapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), userEntity);
		}
		return mapper.map(oidcUser);
	}

	private UserEntity getUserEntity(final String email) {
		return userRepository
				.findByEmail(email)
				.orElse(null);
	}
}
