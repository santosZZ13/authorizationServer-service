package org.authorizationserver.security.mapper;

import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.UserEntity;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OidcUserMapper {
	OidcUser map(OidcUser oidcUser);
	OidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, UserEntity userEntity);
}
