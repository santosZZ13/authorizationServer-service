package org.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.entity.UserEntity;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
	private UUID id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Set<RoleModel> roles;

	public UserModel(UserEntity userEntity) {
		this.email = userEntity.getEmail();
		this.password = userEntity.getPassword();
		this.firstName = userEntity.getFirstName();
		this.lastName = userEntity.getLastName();
		this.roles = userEntity.getRoleEntities().stream()
				.map(RoleModel::fromEntity)
				.collect(java.util.stream.Collectors.toSet());
	}

}
