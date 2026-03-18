package com.fenix_sport.back.application.dto.response;

import java.util.Set;

public record AuthResponse(
		String token,
		Long userId,
		String email,
		Set<String> roles
) {
}

