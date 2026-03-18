package com.fenix_sport.back.application.port.input;

import com.fenix_sport.back.application.dto.request.CreateProductRequest;
import com.fenix_sport.back.application.dto.request.UpdateProductRequest;
import com.fenix_sport.back.application.dto.response.ProductResponse;

import java.util.List;

public interface ProductUseCase {
	ProductResponse create(CreateProductRequest request);

	ProductResponse update(long id, UpdateProductRequest request);

	ProductResponse getById(long id);

	List<ProductResponse> list();

	void delete(long id);
}

