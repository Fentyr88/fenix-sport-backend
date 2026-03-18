package com.fenix_sport.back.application.port.output;

import com.fenix_sport.back.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {
	boolean existsByNombreIgnoreCase(String nombre);

	Optional<Category> findById(long id);

	List<Category> findAll();

	Category save(Category category);

	void deleteById(long id);

	boolean existsById(long id);

	boolean hasProducts(long id);
}

