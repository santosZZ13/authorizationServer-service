package org.authorizationserver.configs.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.authorizationserver.dao.RoleDaoRepository;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.repository.RoleRepository;
import org.authorizationserver.persistent.repository.UserRepository;
import org.authorizationserver.configs.model.CustomOidcUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserServiceOAuth2UserHandler implements Consumer<OidcUser> {

	private final RoleDaoRepository roleDaoRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Override
	public void accept(OidcUser user) {
		CustomOidcUser oidcUser = (CustomOidcUser) user;
		if (oidcUser.getId() == null && this.userRepository.findByEmail(user.getName()).isEmpty()) {
			Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) oidcUser.getAuthorities();
			UserEntity instantUserEntity = oidcUser.toInstantUserEntity();
			RoleEntity defaultRoleEntity = roleRepository.getDefaultRoleEntity();

			if (defaultRoleEntity != null) {
				instantUserEntity.setRoleEntities(Set.of(defaultRoleEntity));
			}
			this.userRepository.save(instantUserEntity);

			if (!CollectionUtils.isEmpty(instantUserEntity.getRoleEntities())) {
				Set<? extends GrantedAuthority> authorities = instantUserEntity.getRoleEntities().stream()
						.flatMap(role -> role.getAuthorities().stream()
								.map(authority -> new SimpleGrantedAuthority(authority.getName()))
						)
						.collect(Collectors.toSet());

				grantedAuthorities.addAll(authorities);
			}
			oidcUser.setId(instantUserEntity.getId());
		}

		log.info("User {} has logged in", user.getName());
	}

	@Override
	public @NotNull Consumer<OidcUser> andThen(@NotNull Consumer<? super OidcUser> after) {
		return Consumer.super.andThen(after);
	}
}
