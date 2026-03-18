package com.fenix_sport.back.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank(message = "email es obligatorio")
		@Email(message = "email inválido")
		String email,

		@NotBlank(message = "password es obligatorio")
		String password
) {
}

