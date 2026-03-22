package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.LoginRequest;
import com.fenix_sport.back.application.dto.request.RegisterRequest;
import com.fenix_sport.back.application.dto.response.AuthResponse;
import com.fenix_sport.back.application.port.input.AuthUseCase;
import com.fenix_sport.back.domain.exception.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthControllerTest {

	@Test
	void register_delegatesToUseCase() {
		AuthUseCase useCase = mock(AuthUseCase.class);
		AuthController controller = new AuthController(useCase);
		RegisterRequest req = new RegisterRequest("Juan", "a@a.com", "secret1", null);
		AuthResponse expected = new AuthResponse("TOKEN", 1L, "a@a.com", Set.of("USER"));
		when(useCase.register(req)).thenReturn(expected);

		AuthResponse resp = controller.register(req);

		assertThat(resp).isEqualTo(expected);
		verify(useCase).register(req);
	}

	@Test
	void login_delegatesToUseCase() {
		AuthUseCase useCase = mock(AuthUseCase.class);
		AuthController controller = new AuthController(useCase);
		LoginRequest req = new LoginRequest("a@a.com", "secret1");
		AuthResponse expected = new AuthResponse("TOKEN", 1L, "a@a.com", Set.of("USER"));
		when(useCase.login(req)).thenReturn(expected);

		AuthResponse resp = controller.login(req);

		assertThat(resp).isEqualTo(expected);
		verify(useCase).login(req);
	}

	@Test
	void login_propagatesDomainException() {
		AuthUseCase useCase = mock(AuthUseCase.class);
		AuthController controller = new AuthController(useCase);
		LoginRequest req = new LoginRequest("a@a.com", "bad");
		when(useCase.login(req)).thenThrow(new NotFoundException("Credenciales inválidas"));

		assertThatThrownBy(() -> controller.login(req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Credenciales inválidas");
	}
}

