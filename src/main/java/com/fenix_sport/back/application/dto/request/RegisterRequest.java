package com.fenix_sport.back.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		@NotBlank(message = "nombre es obligatorio")
		String nombre,

		@NotBlank(message = "email es obligatorio")
		@Email(message = "email inválido")
		String email,

		@NotBlank(message = "password es obligatorio")
		@Size(min = 6, message = "password debe tener al menos 6 caracteres")
		String password,

		String telefono
) {
}

