package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.LoginRequest;
import com.fenix_sport.back.application.dto.request.RegisterRequest;
import com.fenix_sport.back.application.dto.response.AuthResponse;
import com.fenix_sport.back.application.port.input.AuthUseCase;
import com.fenix_sport.back.application.port.output.JwtTokenPort;
import com.fenix_sport.back.application.port.output.PasswordEncoderPort;
import com.fenix_sport.back.application.port.output.RoleRepositoryPort;
import com.fenix_sport.back.application.port.output.UserRepositoryPort;
import com.fenix_sport.back.domain.exception.ConflictException;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Role;
import com.fenix_sport.back.domain.model.RoleName;
import com.fenix_sport.back.domain.model.User;

import java.time.LocalDate;
import java.util.Set;

public class AuthService implements AuthUseCase {
	private final UserRepositoryPort userRepository;
	private final RoleRepositoryPort roleRepository;
	private final PasswordEncoderPort passwordEncoder;
	private final JwtTokenPort jwtTokenPort;

	public AuthService(UserRepositoryPort userRepository,
	                  RoleRepositoryPort roleRepository,
	                  PasswordEncoderPort passwordEncoder,
	                  JwtTokenPort jwtTokenPort) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenPort = jwtTokenPort;
	}

	@Override
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new ConflictException("El email ya está registrado");
		}

		Role userRole = roleRepository.findByName(RoleName.USER)
				.orElseThrow(() -> new NotFoundException("Rol USER no existe"));

		User toSave = new User(
				null,
				request.nombre(),
				request.email(),
				passwordEncoder.encode(request.password()),
				request.telefono(),
				true,
				LocalDate.now(),
				Set.of(userRole)
		);

		User saved = userRepository.save(toSave);
		String token = jwtTokenPort.generateToken(saved);
		return new AuthResponse(
				token,
				saved.id(),
				saved.email(),
				saved.roles() == null ? Set.of() : saved.roles().stream().map(r -> r.name().name()).collect(java.util.stream.Collectors.toSet())
		);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new NotFoundException("Credenciales inválidas"));

		if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
			throw new NotFoundException("Credenciales inválidas");
		}
		if (!user.activo()) {
			throw new ConflictException("Usuario inactivo");
		}
		String token = jwtTokenPort.generateToken(user);
		return new AuthResponse(
				token,
				user.id(),
				user.email(),
				user.roles() == null ? Set.of() : user.roles().stream().map(r -> r.name().name()).collect(java.util.stream.Collectors.toSet())
		);
	}
}

