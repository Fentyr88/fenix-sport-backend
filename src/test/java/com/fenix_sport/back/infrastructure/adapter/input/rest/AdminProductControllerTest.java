package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.CreateProductRequest;
import com.fenix_sport.back.application.port.input.ProductUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class AdminProductControllerTest {
	@Test
	void create_delegatesToUseCase() {
		ProductUseCase useCase = mock(ProductUseCase.class);
		AdminProductController controller = new AdminProductController(useCase);
		CreateProductRequest req = new CreateProductRequest("P", null, new BigDecimal("1.00"), 1, null, 1L);

		controller.create(req);

		verify(useCase).create(req);
	}

	@Test
	void delete_delegatesToUseCase() {
		ProductUseCase useCase = mock(ProductUseCase.class);
		AdminProductController controller = new AdminProductController(useCase);

		controller.delete(10L);

		verify(useCase).delete(10L);
	}
}

