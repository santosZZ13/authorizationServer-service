package org.authorizationserver.service;

import org.authorizationserver.persistent.entity.User;

public interface UserService {
	User getUserByEmail(String email);
	User save(User user);
}
