package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.CreateProductRequest;
import com.fenix_sport.back.application.dto.request.UpdateProductRequest;
import com.fenix_sport.back.application.dto.response.ProductResponse;
import com.fenix_sport.back.application.port.input.ProductUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
	private final ProductUseCase productUseCase;

	public AdminProductController(ProductUseCase productUseCase) {
		this.productUseCase = productUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
		return productUseCase.create(request);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable long id, @Valid @RequestBody UpdateProductRequest request) {
		return productUseCase.update(id, request);
	}

	@GetMapping("/{id}")
	public ProductResponse getById(@PathVariable long id) {
		return productUseCase.getById(id);
	}

	@GetMapping
	public List<ProductResponse> list() {
		return productUseCase.list();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		productUseCase.delete(id);
	}
}

