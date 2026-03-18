package com.fenix_sport.back.application.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
		Long id,
		String nombre,
		String descripcion,
		BigDecimal precio,
		int stock,
		String imagenUrl,
		CategoryResponse categoria
) {
}

