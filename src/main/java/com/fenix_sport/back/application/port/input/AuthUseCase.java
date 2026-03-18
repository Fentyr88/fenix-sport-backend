package com.fenix_sport.back.application.port.input;

import com.fenix_sport.back.application.dto.request.LoginRequest;
import com.fenix_sport.back.application.dto.request.RegisterRequest;
import com.fenix_sport.back.application.dto.response.AuthResponse;

public interface AuthUseCase {
	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}

