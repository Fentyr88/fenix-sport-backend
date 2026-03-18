package com.fenix_sport.back.domain.model;

import java.math.BigDecimal;

public record Product(
		Long id,
		String nombre,
		String descripcion,
		BigDecimal precio,
		int stock,
		String imagenUrl,
		Category categoria
) {
}

