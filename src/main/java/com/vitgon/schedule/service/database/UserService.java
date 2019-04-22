package com.vitgon.schedule.service.database;

import java.util.List;

import org.springframework.social.connect.Connection;

import com.vitgon.schedule.model.database.auth.User;
import com.vitgon.schedule.service.database.base.Service;

public interface UserService extends Service<User, Integer> {
	List<User> findByEmail(String email);
	User findByEmailAndProviderId(String email, String providerId);
	User findByUsername(String username);
	List<User> findBySpecificRoles(List<String> roles);
	User createUser(Connection<?> connection);
}
