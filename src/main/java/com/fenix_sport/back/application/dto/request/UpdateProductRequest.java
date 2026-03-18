package com.fenix_sport.back.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductRequest(
		@NotBlank(message = "nombre es obligatorio")
		String nombre,
		String descripcion,
		@NotNull(message = "precio es obligatorio")
		@Positive(message = "precio debe ser mayor que 0")
		BigDecimal precio,
		@NotNull(message = "stock es obligatorio")
		@Min(value = 0, message = "stock debe ser >= 0")
		Integer stock,
		String imagenUrl,
		@NotNull(message = "categoryId es obligatorio")
		Long categoryId
) {
}

