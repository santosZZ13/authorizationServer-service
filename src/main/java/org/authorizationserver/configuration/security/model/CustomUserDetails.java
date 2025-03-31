package org.authorizationserver.configuration.security.model;

import org.authorizationserver.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
	private String password;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
		this.email = username;
		this.authorities = authorities;
	}

	public CustomUserDetails(String username, String password, Collection<String> authorities) {
		this.email = username;
		this.password = password;
		this.authorities = authorities.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public CustomUserDetails(UserModel userModel) {
		this.email = userModel.getEmail();
		this.password = userModel.getPassword();
		this.authorities = userModel.getRoles().stream()
				.flatMap(role -> role.getAuthorities().stream()
						.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
