package com.fenix_sport.back.application.port.input;

import com.fenix_sport.back.application.dto.request.CreateCategoryRequest;
import com.fenix_sport.back.application.dto.request.UpdateCategoryRequest;
import com.fenix_sport.back.application.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryUseCase {
	CategoryResponse create(CreateCategoryRequest request);

	CategoryResponse update(long id, UpdateCategoryRequest request);

	CategoryResponse getById(long id);

	List<CategoryResponse> list();

	void delete(long id);
}

