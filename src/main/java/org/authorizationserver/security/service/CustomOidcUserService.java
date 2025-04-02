package org.authorizationserver.security.service;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.security.mapper.OidcUserMapper;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.model.UserModel;
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

	private final Map<String, OidcUserMapper> mappers;
	private final UserDaoRepository userDaoRepository;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for such registrationId");
		OidcUserMapper mapper = mappers.get(userRequest.getClientRegistration().getRegistrationId());

		String email = userRequest.getIdToken().getEmail();
		UserModel localUserModel = userDaoRepository.findByEmail(email);
		if (localUserModel != null) {
			return mapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), localUserModel);
		}
		//Map unregistered user
		return mapper.map(oidcUser);
	}
}
