package org.authorizationserver.dao;

import org.authorizationserver.model.UserModel;
import org.springframework.security.core.userdetails.User;

public interface UserDaoRepository {
	UserModel findByEmail(String email);

	void saveUserModel(UserModel userModel);
}
