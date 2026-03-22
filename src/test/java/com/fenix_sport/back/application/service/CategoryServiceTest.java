package com.fenix_sport.back.application.service;

import com.fenix_sport.back.application.dto.request.CreateCategoryRequest;
import com.fenix_sport.back.application.dto.request.UpdateCategoryRequest;
import com.fenix_sport.back.application.dto.response.CategoryResponse;
import com.fenix_sport.back.application.port.output.CategoryRepositoryPort;
import com.fenix_sport.back.domain.exception.ConflictException;
import com.fenix_sport.back.domain.exception.NotFoundException;
import com.fenix_sport.back.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@Mock
	private CategoryRepositoryPort categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	void create_whenNameExists_throwsConflict() {
		CreateCategoryRequest req = new CreateCategoryRequest("Futbol", "desc");
		when(categoryRepository.existsByNombreIgnoreCase(req.nombre())).thenReturn(true);

		assertThatThrownBy(() -> categoryService.create(req))
				.isInstanceOf(ConflictException.class);

		verify(categoryRepository, never()).save(any());
	}

	@Test
	void create_happyPath_savesAndReturnsResponse() {
		CreateCategoryRequest req = new CreateCategoryRequest("Futbol", "desc");
		when(categoryRepository.existsByNombreIgnoreCase(req.nombre())).thenReturn(false);
		when(categoryRepository.save(any(Category.class))).thenReturn(new Category(1L, req.nombre(), req.descripcion()));

		CategoryResponse resp = categoryService.create(req);

		assertThat(resp.id()).isEqualTo(1L);
		assertThat(resp.nombre()).isEqualTo("Futbol");
		assertThat(resp.descripcion()).isEqualTo("desc");
	}

	@Test
	void update_whenCategoryDoesNotExist_throwsNotFound() {
		when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.update(99L, new UpdateCategoryRequest("x", "y")))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("Categoría no encontrada");
	}

	@Test
	void update_happyPath_updatesAndReturnsResponse() {
		Category existing = new Category(5L, "Old", "OldDesc");
		when(categoryRepository.findById(5L)).thenReturn(Optional.of(existing));
		when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

		CategoryResponse resp = categoryService.update(5L, new UpdateCategoryRequest("New", "NewDesc"));

		assertThat(resp.id()).isEqualTo(5L);
		assertThat(resp.nombre()).isEqualTo("New");
		assertThat(resp.descripcion()).isEqualTo("NewDesc");
	}

	@Test
	void getById_whenMissing_throwsNotFound() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> categoryService.getById(1L))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void list_returnsAllMapped() {
		when(categoryRepository.findAll()).thenReturn(List.of(
				new Category(1L, "A", "DA"),
				new Category(2L, "B", null)
		));

		List<CategoryResponse> res = categoryService.list();

		assertThat(res).hasSize(2);
		assertThat(res.get(0).id()).isEqualTo(1L);
		assertThat(res.get(1).nombre()).isEqualTo("B");
	}

	@Test
	void delete_whenNotExists_throwsNotFound() {
		when(categoryRepository.existsById(9L)).thenReturn(false);

		assertThatThrownBy(() -> categoryService.delete(9L))
				.isInstanceOf(NotFoundException.class);

		verify(categoryRepository, never()).deleteById(anyLong());
	}

	@Test
	void delete_whenHasProducts_throwsConflict() {
		when(categoryRepository.existsById(9L)).thenReturn(true);
		when(categoryRepository.hasProducts(9L)).thenReturn(true);

		assertThatThrownBy(() -> categoryService.delete(9L))
				.isInstanceOf(ConflictException.class)
				.hasMessageContaining("productos");

		verify(categoryRepository, never()).deleteById(anyLong());
	}

	@Test
	void delete_happyPath_deletes() {
		when(categoryRepository.existsById(9L)).thenReturn(true);
		when(categoryRepository.hasProducts(9L)).thenReturn(false);

		categoryService.delete(9L);

		verify(categoryRepository).deleteById(9L);
	}
}

