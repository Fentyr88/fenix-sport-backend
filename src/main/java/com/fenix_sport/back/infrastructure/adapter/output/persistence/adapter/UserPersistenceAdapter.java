package com.fenix_sport.back.infrastructure.adapter.output.persistence.adapter;

import com.fenix_sport.back.application.port.output.UserRepositoryPort;
import com.fenix_sport.back.domain.model.User;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.RoleEntity;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper.UserMapper;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaUserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

public class UserPersistenceAdapter implements UserRepositoryPort {
	private final JpaUserRepository userRepository;

	public UserPersistenceAdapter(JpaUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email).map(UserMapper::toDomain);
	}

	@Override
	public Optional<User> findById(long id) {
		return userRepository.findById(id).map(UserMapper::toDomain);
	}

	@Override
	@Transactional
	public User save(User user) {
		UserEntity entity = new UserEntity();
		entity.setId(user.id());
		entity.setNombre(user.nombre());
		entity.setEmail(user.email());
		entity.setPassword(user.passwordHash());
		entity.setTelefono(user.telefono());
		entity.setActivo(user.activo());
		entity.setFechaRegistro(user.fechaRegistro());
		entity.setRoles(new HashSet<>());
		if (user.roles() != null) {
			for (var r : user.roles()) {
				RoleEntity re = new RoleEntity();
				re.setId(r.id());
				re.setName(r.name());
				entity.getRoles().add(re);
			}
		}
		return UserMapper.toDomain(userRepository.save(entity));
	}
}

