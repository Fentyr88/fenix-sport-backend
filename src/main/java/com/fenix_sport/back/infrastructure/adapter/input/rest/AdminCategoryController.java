package com.fenix_sport.back.infrastructure.adapter.input.rest;

import com.fenix_sport.back.application.dto.request.CreateCategoryRequest;
import com.fenix_sport.back.application.dto.request.UpdateCategoryRequest;
import com.fenix_sport.back.application.dto.response.CategoryResponse;
import com.fenix_sport.back.application.port.input.CategoryUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {
	private final CategoryUseCase categoryUseCase;

	public AdminCategoryController(CategoryUseCase categoryUseCase) {
		this.categoryUseCase = categoryUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryResponse create(@Valid @RequestBody CreateCategoryRequest request) {
		return categoryUseCase.create(request);
	}

	@PutMapping("/{id}")
	public CategoryResponse update(@PathVariable long id, @Valid @RequestBody UpdateCategoryRequest request) {
		return categoryUseCase.update(id, request);
	}

	@GetMapping("/{id}")
	public CategoryResponse getById(@PathVariable long id) {
		return categoryUseCase.getById(id);
	}

	@GetMapping
	public List<CategoryResponse> list() {
		return categoryUseCase.list();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		categoryUseCase.delete(id);
	}
}

