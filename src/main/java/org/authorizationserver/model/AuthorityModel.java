package org.authorizationserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.persistent.entity.AuthorityEntity;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityModel {
	protected Integer id;
	private String name;

	public static AuthorityModel fromEntity(AuthorityEntity authorityEntity) {
		return AuthorityModel.builder()
				.id(authorityEntity.getId())
				.name(authorityEntity.getName())
				.build();
	}

	public AuthorityEntity toEntity() {
		AuthorityEntity authorityEntity = new AuthorityEntity();
		authorityEntity.setId(this.getId());
		authorityEntity.setName(this.getName());
		return authorityEntity;
	}
}
