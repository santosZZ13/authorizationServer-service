package org.authorizationserver.security.handler;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.dao.RoleDaoRepository;
import org.authorizationserver.dao.UserDaoRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserServiceOAuth2UserHandler implements Consumer<OidcUser> {

	private final UserDaoRepository userDaoRepository;
	private final RoleDaoRepository roleDaoRepository;

	@Override
	public void accept(OidcUser user) {
//		// Capture user in a local data store on first authentication
//		CustomOidcUser oidcUser = (CustomOidcUser) user;
//		if (oidcUser.getId() == null && this.userDaoRepository.findByEmail(user.getName()) == null) {
//			Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) oidcUser.getAuthorities();
//			UserModel localUserModel = oidcUser.toInstantUser();
//			RoleModel defaultRole = roleDaoRepository.getDefaultRole();
//
//			if (defaultRole != null) {
//				localUserModel.setRoles(null);
//				localUserModel.setRoles(Set.of(defaultRole));
//			}
//			this.userDaoRepository.saveUserModel(localUserModel);
//			if (!CollectionUtils.isEmpty(localUserModel.getRoles())) {
//				Set<? extends GrantedAuthority> authorities =
//						localUserModel.getRoles().stream()
//								.flatMap(role -> role.getAuthorities().stream()
//										.map(authority -> new SimpleGrantedAuthority(authority.getName()))
//								)
//								.collect(Collectors.toSet());
//				grantedAuthorities.addAll(authorities);
//			}
//
//			oidcUser.setId(localUserModel.getId());
//		}
	}

	@Override
	public @NotNull Consumer<OidcUser> andThen(@NotNull Consumer<? super OidcUser> after) {
		return Consumer.super.andThen(after);
	}
}
