package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.LoginRequest;
import com.fenix_sport.back.application.dto.request.RegisterRequest;
import com.fenix_sport.back.application.dto.response.AuthResponse;
import com.fenix_sport.back.application.port.output.JwtTokenPort;
import com.fenix_sport.back.application.port.output.PasswordEncoderPort;
import com.fenix_sport.back.application.port.output.RoleRepositoryPort;
import com.fenix_sport.back.application.port.output.UserRepositoryPort;
import com.fenix_sport.back.domain.exception.ConflictException;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Role;
import com.fenix_sport.back.domain.model.RoleName;
import com.fenix_sport.back.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepositoryPort userRepository;
	@Mock
	private RoleRepositoryPort roleRepository;
	@Mock
	private PasswordEncoderPort passwordEncoder;
	@Mock
	private JwtTokenPort jwtTokenPort;

	@InjectMocks
	private AuthService authService;

	@Test
	void register_whenEmailAlreadyExists_throwsConflict() {
		RegisterRequest req = new RegisterRequest("Juan", "juan@mail.com", "secret1", "123");
		when(userRepository.existsByEmail(req.email())).thenReturn(true);

		assertThatThrownBy(() -> authService.register(req))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("email");

		verify(roleRepository, never()).findByName(any());
		verify(userRepository, never()).save(any());
	}

	@Test
	void register_whenUserRoleMissing_throwsNotFound() {
		RegisterRequest req = new RegisterRequest("Juan", "juan@mail.com", "secret1", "123");
		when(userRepository.existsByEmail(req.email())).thenReturn(false);
		when(roleRepository.findByName(RoleName.USER)).thenReturn(java.util.Optional.empty());

		assertThatThrownBy(() -> authService.register(req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Rol USER");

		verify(userRepository, never()).save(any());
		verify(jwtTokenPort, never()).generateToken(any());
	}

	@Test
	void register_happyPath_encodesPassword_savesUser_andReturnsToken() {
		RegisterRequest req = new RegisterRequest("Juan", "juan@mail.com", "secret1", "123");
		Role userRole = new Role(10L, RoleName.USER);

		when(userRepository.existsByEmail(req.email())).thenReturn(false);
		when(roleRepository.findByName(RoleName.USER)).thenReturn(java.util.Optional.of(userRole));
		when(passwordEncoder.encode(req.password())).thenReturn("ENC");

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> {
			User u = inv.getArgument(0);
			return new User(1L, u.nombre(), u.email(), u.passwordHash(), u.telefono(), u.activo(), u.fechaRegistro(), u.roles());
		});
		when(jwtTokenPort.generateToken(any(User.class))).thenReturn("TOKEN");

		AuthResponse resp = authService.register(req);

		User savedArg = userCaptor.getValue();
		assertThat(savedArg.id()).isNull();
		assertThat(savedArg.nombre()).isEqualTo("Juan");
		assertThat(savedArg.email()).isEqualTo("juan@mail.com");
		assertThat(savedArg.passwordHash()).isEqualTo("ENC");
		assertThat(savedArg.activo()).isTrue();
		assertThat(savedArg.fechaRegistro()).isNotNull();
		assertThat(savedArg.fechaRegistro()).isInstanceOf(LocalDate.class);
		assertThat(savedArg.roles()).containsExactly(userRole);

		assertThat(resp.token()).isEqualTo("TOKEN");
		assertThat(resp.userId()).isEqualTo(1L);
		assertThat(resp.email()).isEqualTo("juan@mail.com");
		assertThat(resp.roles()).containsExactlyInAnyOrder("USER");

		verify(jwtTokenPort).generateToken(any(User.class));
	}

	@Test
	void login_whenUserNotFound_throwsNotFoundInvalidCredentials() {
		LoginRequest req = new LoginRequest("missing@mail.com", "x");
		when(userRepository.findByEmail(req.email())).thenReturn(java.util.Optional.empty());

		assertThatThrownBy(() -> authService.login(req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Credenciales inválidas");

		verify(passwordEncoder, never()).matches(any(), any());
		verify(jwtTokenPort, never()).generateToken(any());
	}

	@Test
	void login_whenPasswordDoesNotMatch_throwsNotFoundInvalidCredentials() {
		User user = new User(1L, "Juan", "juan@mail.com", "HASH", null, true, LocalDate.now(), Set.of());
		LoginRequest req = new LoginRequest(user.email(), "bad");
		when(userRepository.findByEmail(req.email())).thenReturn(java.util.Optional.of(user));
		when(passwordEncoder.matches(req.password(), user.passwordHash())).thenReturn(false);

		assertThatThrownBy(() -> authService.login(req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Credenciales inválidas");

		verify(jwtTokenPort, never()).generateToken(any());
	}

	@Test
	void login_whenUserInactive_throwsConflict() {
		User user = new User(1L, "Juan", "juan@mail.com", "HASH", null, false, LocalDate.now(), Set.of());
		LoginRequest req = new LoginRequest(user.email(), "secret1");
		when(userRepository.findByEmail(req.email())).thenReturn(java.util.Optional.of(user));
		when(passwordEncoder.matches(req.password(), user.passwordHash())).thenReturn(true);

		assertThatThrownBy(() -> authService.login(req))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("inactivo");

		verify(jwtTokenPort, never()).generateToken(any());
	}

	@Test
	void login_happyPath_returnsTokenAndUserInfo() {
		Role role = new Role(2L, RoleName.USER);
		User user = new User(1L, "Juan", "juan@mail.com", "HASH", null, true, LocalDate.now(), Set.of(role));
		LoginRequest req = new LoginRequest(user.email(), "secret1");

		when(userRepository.findByEmail(req.email())).thenReturn(java.util.Optional.of(user));
		when(passwordEncoder.matches(req.password(), user.passwordHash())).thenReturn(true);
		when(jwtTokenPort.generateToken(user)).thenReturn("TOKEN");

		AuthResponse resp = authService.login(req);

		assertThat(resp.token()).isEqualTo("TOKEN");
		assertThat(resp.userId()).isEqualTo(1L);
		assertThat(resp.email()).isEqualTo("juan@mail.com");
		assertThat(resp.roles()).containsExactlyInAnyOrder("USER");
	}
}

