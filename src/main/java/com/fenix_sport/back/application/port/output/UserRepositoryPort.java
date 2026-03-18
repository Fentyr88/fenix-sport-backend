package com.fenix_sport.back.application.port.output;

import com.fenix_sport.back.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findById(long id);

	User save(User user);
}

