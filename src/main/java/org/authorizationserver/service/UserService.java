package org.authorizationserver.service;

import org.authorizationserver.persistent.entity.UserEntity;

public interface UserService {
	UserEntity getUserByEmail(String email);
	UserEntity save(UserEntity userEntity);
}
