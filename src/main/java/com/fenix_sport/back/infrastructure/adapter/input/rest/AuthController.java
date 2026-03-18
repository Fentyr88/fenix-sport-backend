package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.LoginRequest;
import com.fenix_sport.back.application.dto.request.RegisterRequest;
import com.fenix_sport.back.application.dto.response.AuthResponse;
import com.fenix_sport.back.application.port.input.AuthUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthUseCase authUseCase;

	public AuthController(AuthUseCase authUseCase) {
		this.authUseCase = authUseCase;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
		return authUseCase.register(request);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody LoginRequest request) {
		return authUseCase.login(request);
	}
}

