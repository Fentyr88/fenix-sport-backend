package com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper;

import com.fenix_sport.back.domain.model.User;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.UserEntity;

import java.util.stream.Collectors;

public final class UserMapper {
	private UserMapper() {
	}

	public static User toDomain(UserEntity entity) {
		if (entity == null) return null;
		return new User(
				entity.getId(),
				entity.getNombre(),
				entity.getEmail(),
				entity.getPassword(),
				entity.getTelefono(),
				entity.isActivo(),
				entity.getFechaRegistro(),
				entity.getRoles().stream().map(RoleMapper::toDomain).collect(Collectors.toSet())
		);
	}
}

