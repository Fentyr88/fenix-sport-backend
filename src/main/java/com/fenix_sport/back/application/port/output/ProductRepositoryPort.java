package com.fenix_sport.back.application.port.output;

import com.fenix_sport.back.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
	Optional<Product> findById(long id);

	List<Product> findAll();

	Product save(Product product);

	void deleteById(long id);

	boolean existsById(long id);
}

