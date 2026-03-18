package com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper;

import com.fenix_sport.back.domain.model.Category;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.CategoryEntity;

public final class CategoryMapper {
	private CategoryMapper() {
	}

	public static Category toDomain(CategoryEntity entity) {
		if (entity == null) return null;
		return new Category(entity.getId(), entity.getNombre(), entity.getDescripcion());
	}

	public static CategoryEntity toEntity(Category domain) {
		if (domain == null) return null;
		CategoryEntity e = new CategoryEntity();
		e.setId(domain.id());
		e.setNombre(domain.nombre());
		e.setDescripcion(domain.descripcion());
		return e;
	}
}

