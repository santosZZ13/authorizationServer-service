package org.authorizationserver.service.impl;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.mapper.OidcUserMapper;
import org.authorizationserver.persistent.entity.User;
import org.authorizationserver.service.UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

	private final UserService userService;
	private final Map<String, OidcUserMapper> mappers;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
 		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for such registrationId");

		OidcUserMapper oidcUserMapper = mappers.get(registrationId);
		String email = userRequest.getIdToken().getEmail();
		User localUser = userService.getUserByEmail(email);

		if (localUser != null) {
			return oidcUserMapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), localUser);
		}

		//Map unregistered user
		return oidcUserMapper.map(oidcUser);

	}
}
