package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.CreateProductRequest;
import com.fenix_sport.back.application.dto.request.UpdateProductRequest;
import com.fenix_sport.back.application.dto.response.CategoryResponse;
import com.fenix_sport.back.application.dto.response.ProductResponse;
import com.fenix_sport.back.application.port.input.ProductUseCase;
import com.fenix_sport.back.application.port.output.CategoryRepositoryPort;
import com.fenix_sport.back.application.port.output.ProductRepositoryPort;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Category;
import com.fenix_sport.back.domain.model.Product;

import java.util.List;

public class ProductService implements ProductUseCase {
	private final ProductRepositoryPort productRepository;
	private final CategoryRepositoryPort categoryRepository;

	public ProductService(ProductRepositoryPort productRepository, CategoryRepositoryPort categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ProductResponse create(CreateProductRequest request) {
		Category category = categoryRepository.findById(request.categoryId())
				.orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

		Product saved = productRepository.save(new Product(
				null,
				request.nombre(),
				request.descripcion(),
				request.precio(),
				request.stock(),
				request.imagenUrl(),
				category
		));
		return toResponse(saved);
	}

	@Override
	public ProductResponse update(long id, UpdateProductRequest request) {
		Product existing = productRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Producto no encontrado"));

		Category category = categoryRepository.findById(request.categoryId())
				.orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

		Product toSave = new Product(
				existing.id(),
				request.nombre(),
				request.descripcion(),
				request.precio(),
				request.stock(),
				request.imagenUrl(),
				category
		);
		return toResponse(productRepository.save(toSave));
	}

	@Override
	public ProductResponse getById(long id) {
		return productRepository.findById(id)
				.map(this::toResponse)
				.orElseThrow(() -> new NotFoundException("Producto no encontrado"));
	}

	@Override
	public List<ProductResponse> list() {
		return productRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public void delete(long id) {
		if (!productRepository.existsById(id)) {
			throw new NotFoundException("Producto no encontrado");
		}
		productRepository.deleteById(id);
	}

	private ProductResponse toResponse(Product p) {
		CategoryResponse category = p.categoria() == null
				? null
				: new CategoryResponse(p.categoria().id(), p.categoria().nombre(), p.categoria().descripcion());

		return new ProductResponse(
				p.id(),
				p.nombre(),
				p.descripcion(),
				p.precio(),
				p.stock(),
				p.imagenUrl(),
				category
		);
	}
}

