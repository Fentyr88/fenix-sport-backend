package com.fenix_sport.back.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
		@NotBlank(message = "nombre es obligatorio")
		String nombre,
		String descripcion
) {
}

