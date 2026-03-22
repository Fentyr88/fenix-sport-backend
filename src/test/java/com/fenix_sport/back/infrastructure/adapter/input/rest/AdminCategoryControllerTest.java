package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.CreateCategoryRequest;
import com.fenix_sport.back.application.port.input.CategoryUseCase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminCategoryControllerTest {
	@Test
	void create_delegatesToUseCase() {
		CategoryUseCase useCase = mock(CategoryUseCase.class);
		AdminCategoryController controller = new AdminCategoryController(useCase);
		CreateCategoryRequest req = new CreateCategoryRequest("Cat", null);

		controller.create(req);

		verify(useCase).create(req);
	}

	@Test
	void delete_delegatesToUseCase() {
		CategoryUseCase useCase = mock(CategoryUseCase.class);
		AdminCategoryController controller = new AdminCategoryController(useCase);

		controller.delete(10L);

		verify(useCase).delete(10L);
	}
}

