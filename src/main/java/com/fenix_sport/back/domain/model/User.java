package com.fenix_sport.back.domain.model;

import java.time.LocalDate;
import java.util.Set;

public record User(
		Long id,
		String nombre,
		String email,
		String passwordHash,
		String telefono,
		boolean activo,
		LocalDate fechaRegistro,
		Set<Role> roles
) {
}

