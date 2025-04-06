package org.authorizationserver.dao;

import lombok.AllArgsConstructor;
import org.authorizationserver.model.RoleModel;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.repository.RoleRepository;
import org.authorizationserver.persistent.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Repository
@AllArgsConstructor
public class UserDaoRepositoryImpl implements UserDaoRepository {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Override
	public UserModel findByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email)
				.orElse(null);
		if (userEntity != null) {
			return new UserModel(userEntity);
		}

		return null;
	}

	@Override
	public void saveUserModel(UserModel userModel) {
		userRepository.save(toEntity(userModel));
	}

	private UserEntity toEntity(UserModel userModel) {
		RoleEntity defaultRoleEntity = roleRepository.getDefaultRoleEntity();
		return UserEntity.builder()
				.email(userModel.getEmail())
				.password(userModel.getPassword())
				.firstName(userModel.getFirstName())
				.lastName(userModel.getLastName())
				.locale(userModel.getLocale())
				.avatarUrl(userModel.getAvatarUrl())
				.provider(userModel.getProvider())
				.roleEntities(Set.of(defaultRoleEntity))
				.active(userModel.isActive())
				.emailVerified(userModel.isEmailVerified())
				.birthMonth(userModel.getBirthMonth())
				.birthDay(userModel.getBirthDay())
				.birthYear(userModel.getBirthYear())
				.build();
	}
}