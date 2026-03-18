package com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper;

import com.fenix_sport.back.domain.model.Role;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.RoleEntity;

public final class RoleMapper {
	private RoleMapper() {
	}

	public static Role toDomain(RoleEntity entity) {
		if (entity == null) return null;
		return new Role(entity.getId(), entity.getName());
	}

	public static RoleEntity toEntity(Role domain) {
		if (domain == null) return null;
		RoleEntity e = new RoleEntity();
		e.setId(domain.id());
		e.setName(domain.name());
		return e;
	}
}

