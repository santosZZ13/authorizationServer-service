package org.authorizationserver.dao.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.dao.UserModelRepository;
import org.authorizationserver.exception.NotFoundUserException;
import org.authorizationserver.mapper.UserMapper;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.RoleEntity;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.repository.RoleRepository;
import org.authorizationserver.persistent.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@AllArgsConstructor
public class UserModelRepositoryImpl implements UserModelRepository {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Override
	public UserModel findByEmail(String email) {
		UserEntity userEntity = userRepository.findByEmail(email)
				.orElse(null);
		if (userEntity != null) {
			return UserMapper.toUserModel(userEntity);
		}
		return null;
	}

	@Override
	public void saveUserModel(UserModel userModel) {
		RoleEntity defaultRoleEntity = roleRepository.getDefaultRoleEntity();
		UserEntity userEntity = UserMapper.toUserEntity(userModel, Set.of(defaultRoleEntity));
		userRepository.save(userEntity);
	}

	@Override
	public void updateUserActiveStatus(String email, boolean active) {
		UserEntity userEntity = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundUserException("User not found", "", ""));
		userEntity.setActive(active);
		userRepository.save(userEntity);
	}
}