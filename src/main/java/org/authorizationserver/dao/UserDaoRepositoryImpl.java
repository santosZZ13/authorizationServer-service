package org.authorizationserver.dao;

import lombok.AllArgsConstructor;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserDaoRepositoryImpl implements UserDaoRepository {
	private final UserRepository userRepository;

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
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(userModel.getEmail());
		userEntity.setPassword(userModel.getPassword());
		userEntity.setFirstName(userModel.getFirstName());
		userEntity.setLastName(userModel.getLastName());
		userRepository.save(userEntity);
	}
}
