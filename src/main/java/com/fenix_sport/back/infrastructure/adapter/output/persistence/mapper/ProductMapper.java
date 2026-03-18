package com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper;

import com.fenix_sport.back.domain.model.Product;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.ProductEntity;

public final class ProductMapper {
	private ProductMapper() {
	}

	public static Product toDomain(ProductEntity entity) {
		if (entity == null) return null;
		return new Product(
				entity.getId(),
				entity.getNombre(),
				entity.getDescripcion(),
				entity.getPrecio(),
				entity.getStock(),
				entity.getImagenUrl(),
				CategoryMapper.toDomain(entity.getCategoria())
		);
	}
}

