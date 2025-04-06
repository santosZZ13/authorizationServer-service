package org.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.enums.Provider;
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
	private String locale;
	private String avatarUrl;
	private boolean emailVerified;
	private boolean active;
	private Provider provider;
	private int birthMonth;
	private int birthDay;
	private int birthYear;
	private Set<RoleModel> roleModels;

	public UserModel(UserEntity userEntity) {
		this.email = userEntity.getEmail();
		this.password = userEntity.getPassword();
		this.firstName = userEntity.getFirstName();
		this.lastName = userEntity.getLastName();
		this.roleModels = userEntity.getRoleEntities().stream()
				.map(RoleModel::fromEntity)
				.collect(java.util.stream.Collectors.toSet());
	}
}
