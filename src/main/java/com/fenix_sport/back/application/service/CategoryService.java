package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.CreateCategoryRequest;
import com.fenix_sport.back.application.dto.request.UpdateCategoryRequest;
import com.fenix_sport.back.application.dto.response.CategoryResponse;
import com.fenix_sport.back.application.port.input.CategoryUseCase;
import com.fenix_sport.back.application.port.output.CategoryRepositoryPort;
import com.fenix_sport.back.domain.exception.ConflictException;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Category;

import java.util.List;

public class CategoryService implements CategoryUseCase {
	private final CategoryRepositoryPort categoryRepository;

	public CategoryService(CategoryRepositoryPort categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public CategoryResponse create(CreateCategoryRequest request) {
		if (categoryRepository.existsByNombreIgnoreCase(request.nombre())) {
			throw new ConflictException("Ya existe una categoría con ese nombre");
		}
		Category saved = categoryRepository.save(new Category(null, request.nombre(), request.descripcion()));
		return toResponse(saved);
	}

	@Override
	public CategoryResponse update(long id, UpdateCategoryRequest request) {
		Category existing = categoryRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

		Category toSave = new Category(existing.id(), request.nombre(), request.descripcion());
		Category saved = categoryRepository.save(toSave);
		return toResponse(saved);
	}

	@Override
	public CategoryResponse getById(long id) {
		return categoryRepository.findById(id)
				.map(this::toResponse)
				.orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
	}

	@Override
	public List<CategoryResponse> list() {
		return categoryRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public void delete(long id) {
		if (!categoryRepository.existsById(id)) {
			throw new NotFoundException("Categoría no encontrada");
		}
		if (categoryRepository.hasProducts(id)) {
			throw new ConflictException("No se puede eliminar: la categoría tiene productos asociados");
		}
		categoryRepository.deleteById(id);
	}

	private CategoryResponse toResponse(Category category) {
		return new CategoryResponse(category.id(), category.nombre(), category.descripcion());
	}
}

