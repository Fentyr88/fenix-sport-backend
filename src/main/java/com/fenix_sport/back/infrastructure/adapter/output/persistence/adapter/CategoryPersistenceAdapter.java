package com.fenix_sport.back.infrastructure.adapter.output.persistence.adapter;

import com.fenix_sport.back.application.port.output.CategoryRepositoryPort;
import com.fenix_sport.back.domain.model.Category;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper.CategoryMapper;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaCategoryRepository;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class CategoryPersistenceAdapter implements CategoryRepositoryPort {
	private final JpaCategoryRepository categoryRepository;
	private final JpaProductRepository productRepository;

	public CategoryPersistenceAdapter(JpaCategoryRepository categoryRepository, JpaProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	public boolean existsByNombreIgnoreCase(String nombre) {
		return categoryRepository.existsByNombreIgnoreCase(nombre);
	}

	@Override
	public Optional<Category> findById(long id) {
		return categoryRepository.findById(id).map(CategoryMapper::toDomain);
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll().stream().map(CategoryMapper::toDomain).toList();
	}

	@Override
	@Transactional
	public Category save(Category category) {
		return CategoryMapper.toDomain(categoryRepository.save(CategoryMapper.toEntity(category)));
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public boolean existsById(long id) {
		return categoryRepository.existsById(id);
	}

	@Override
	public boolean hasProducts(long id) {
		return productRepository.existsByCategoria_Id(id);
	}
}

