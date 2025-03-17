//package org.authorizationserver.service.impl;
//
//import lombok.AllArgsConstructor;
//import org.authorizationserver.persistent.entity.UserEntity;
//import org.authorizationserver.model.CustomUserDetails;
//import org.authorizationserver.service.UserService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
//@Service
//@AllArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//	private final UserService userService;
//
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//		UserEntity userEntityByEmail = userService.getUserByEmail(username);
//
//		if (Objects.isNull(userEntityByEmail)) {
//			throw new UsernameNotFoundException("Unable to found user: " + username);
//		}
//
//		return new CustomUserDetails(userEntityByEmail);
//	}
//}
