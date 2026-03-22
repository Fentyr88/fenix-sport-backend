package com.fenix_sport.back.infrastructure.adapter.output.persistence.adapter;

import com.fenix_sport.back.application.port.output.ProductRepositoryPort;
import com.fenix_sport.back.domain.model.Product;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper.ProductMapper;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaProductRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProductPersistenceAdapter implements ProductRepositoryPort {
	private final JpaProductRepository productRepository;

	public ProductPersistenceAdapter(JpaProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findById(long id) {
		return productRepository.findById(id).map(ProductMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return productRepository.findAll().stream().map(ProductMapper::toDomain).toList();
	}

	@Override
	@Transactional
	public Product save(Product product) {
		// Se persiste vía JPA con una entidad manejada.
		var entity = new com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.ProductEntity();
		entity.setId(product.id());
		entity.setNombre(product.nombre());
		entity.setDescripcion(product.descripcion());
		entity.setPrecio(product.precio());
		entity.setStock(product.stock());
		entity.setImagenUrl(product.imagenUrl());

		var categoryRef = new com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.CategoryEntity();
		categoryRef.setId(product.categoria().id());
		entity.setCategoria(categoryRef);

		return ProductMapper.toDomain(productRepository.save(entity));
	}

	@Override
	@Transactional
	public void deleteById(long id) {
		productRepository.deleteById(id);
	}

	@Override
	public boolean existsById(long id) {
		return productRepository.existsById(id);
	}
}

