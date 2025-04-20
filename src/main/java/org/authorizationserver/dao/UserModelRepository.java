package org.authorizationserver.dao;

import org.authorizationserver.model.UserModel;
import org.authorizationserver.persistent.entity.UserEntity;

public interface UserModelRepository {
	UserModel findByEmail(String email);

	void saveUserModel(UserModel userModel);

	void updateUserActiveStatus(String email, boolean active);
}
