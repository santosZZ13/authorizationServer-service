package org.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.persistent.entity.Role;
import org.authorizationserver.persistent.entity.UserEntity;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Set<String> roles;

	public UserModel(UserEntity userEntity) {
		this.email = userEntity.getEmail();
		this.password = userEntity.getPassword();
		this.firstName = userEntity.getFirstName();
		this.lastName = userEntity.getLastName();
		this.roles = userEntity.getRoles().stream()
				.map(Role::getName)
				.collect(java.util.stream.Collectors.toSet());
	}

}
