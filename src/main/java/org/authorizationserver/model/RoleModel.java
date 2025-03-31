package org.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.persistent.entity.RoleEntity;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
	private Set<AuthorityModel> authorities = new HashSet<>();
	private String name;

	public static RoleModel fromEntity(RoleEntity roleEntity) {
		RoleModel roleModel = new RoleModel();
		roleModel.setName(roleEntity.getName());
		roleEntity.getAuthorities().forEach(authorityEntity -> {
			AuthorityModel authorityModel = AuthorityModel.fromEntity(authorityEntity);
			roleModel.getAuthorities().add(authorityModel);
		});
		return roleModel;
	}
}
