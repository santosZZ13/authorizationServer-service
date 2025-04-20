package org.authorizationserver.mapper;

import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.entity.UserEntity;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class UserMapper {
	private UserMapper() {
	}

	public static UserModel toUserModel(UserEntity entity) {
		if (entity == null) {
			return null;
		}
		return UserModel.builder()
				.id(entity.getId() != null ? UUID.fromString(entity.getId().toString()) : null)
				.email(entity.getEmail())
				.password(entity.getPassword())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.locale(entity.getLocale())
				.avatarUrl(entity.getAvatarUrl())
				.emailVerified(entity.isEmailVerified())
				.active(entity.isActive())
				.provider(entity.getProvider())
				.birthMonth(entity.getBirthMonth())
				.birthDay(entity.getBirthDay())
				.birthYear(entity.getBirthYear())
				.roleModels(entity.getRoleEntities().stream()
						.map(RoleMapper::toRoleModel)
						.collect(Collectors.toSet()))
				.build();
	}


	public static UserEntity toUserEntity(UserModel model, Set<RoleEntity> roleEntities) {
		if (model == null) {
			return null;
		}
		return UserEntity.builder()
				.id(model.getId() != null ? model.getId() : null)
				.email(model.getEmail())
				.password(model.getPassword())
				.firstName(model.getFirstName())
				.lastName(model.getLastName())
				.locale(model.getLocale())
				.avatarUrl(model.getAvatarUrl())
				.emailVerified(model.isEmailVerified())
				.active(model.isActive())
				.provider(model.getProvider())
				.birthMonth(model.getBirthMonth())
				.birthDay(model.getBirthDay())
				.birthYear(model.getBirthYear())
				.roleEntities(roleEntities != null ? roleEntities : Set.of())
				.build();
	}
}
