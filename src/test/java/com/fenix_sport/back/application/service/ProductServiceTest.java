package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.CreateProductRequest;
import com.fenix_sport.back.application.dto.request.UpdateProductRequest;
import com.fenix_sport.back.application.dto.response.ProductResponse;
import com.fenix_sport.back.application.port.output.CategoryRepositoryPort;
import com.fenix_sport.back.application.port.output.ProductRepositoryPort;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Category;
import com.fenix_sport.back.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@Mock
	private ProductRepositoryPort productRepository;
	@Mock
	private CategoryRepositoryPort categoryRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void create_whenCategoryMissing_throwsNotFound() {
		CreateProductRequest req = new CreateProductRequest("P", "D", new BigDecimal("10.00"), 2, null, 99L);
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.create(req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Categoría");
		verify(productRepository, never()).save(any());
	}

	@Test
	void create_happyPath_savesAndMapsResponse() {
		Category cat = new Category(1L, "Cat", "CD");
		CreateProductRequest req = new CreateProductRequest("P", "D", new BigDecimal("10.00"), 2, "img", 1L);
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
		when(productRepository.save(any(Product.class))).thenReturn(new Product(7L, req.nombre(), req.descripcion(), req.precio(), req.stock(), req.imagenUrl(), cat));

		ProductResponse resp = productService.create(req);
		assertThat(resp.id()).isEqualTo(7L);
		assertThat(resp.nombre()).isEqualTo("P");
		assertThat(resp.categoria()).isNotNull();
		assertThat(resp.categoria().id()).isEqualTo(1L);
	}

	@Test
	void update_whenProductMissing_throwsNotFound() {
		when(productRepository.findById(5L)).thenReturn(Optional.empty());
		UpdateProductRequest req = new UpdateProductRequest("P", "D", new BigDecimal("10.00"), 2, null, 1L);

		assertThatThrownBy(() -> productService.update(5L, req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Producto");
	}

	@Test
	void update_whenCategoryMissing_throwsNotFound() {
		Product existing = new Product(5L, "Old", null, new BigDecimal("1.00"), 1, null, null);
		when(productRepository.findById(5L)).thenReturn(Optional.of(existing));
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
		UpdateProductRequest req = new UpdateProductRequest("P", "D", new BigDecimal("10.00"), 2, null, 99L);

		assertThatThrownBy(() -> productService.update(5L, req))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Categoría");
	}

	@Test
	void update_happyPath_savesAndMapsResponse() {
		Category cat = new Category(3L, "Cat", null);
		Product existing = new Product(5L, "Old", null, new BigDecimal("1.00"), 1, null, cat);
		UpdateProductRequest req = new UpdateProductRequest("New", "Desc", new BigDecimal("10.00"), 2, "img", 3L);

		when(productRepository.findById(5L)).thenReturn(Optional.of(existing));
		when(categoryRepository.findById(3L)).thenReturn(Optional.of(cat));
		when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

		ProductResponse resp = productService.update(5L, req);
		assertThat(resp.id()).isEqualTo(5L);
		assertThat(resp.nombre()).isEqualTo("New");
		assertThat(resp.categoria().id()).isEqualTo(3L);
	}

	@Test
	void getById_whenMissing_throwsNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> productService.getById(1L))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void list_mapsAll() {
		when(productRepository.findAll()).thenReturn(List.of(
				new Product(1L, "A", null, new BigDecimal("1.00"), 1, null, null),
				new Product(2L, "B", null, new BigDecimal("2.00"), 2, null, new Category(9L, "C", null))
		));

		List<ProductResponse> res = productService.list();
		assertThat(res).hasSize(2);
		assertThat(res.get(0).categoria()).isNull();
		assertThat(res.get(1).categoria()).isNotNull();
	}

	@Test
	void delete_whenNotExists_throwsNotFound() {
		when(productRepository.existsById(9L)).thenReturn(false);

		assertThatThrownBy(() -> productService.delete(9L))
				.isInstanceOf(NotFoundException.class);

		verify(productRepository, never()).deleteById(anyLong());
	}

	@Test
	void delete_happyPath_deletes() {
		when(productRepository.existsById(9L)).thenReturn(true);

		productService.delete(9L);

		verify(productRepository).deleteById(9L);
	}
}

