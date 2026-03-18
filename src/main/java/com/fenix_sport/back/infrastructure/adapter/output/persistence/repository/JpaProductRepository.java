package com.fenix_sport.back.infrastructure.adapter.output.persistence.repository;

import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
	boolean existsByCategoria_Id(Long categoriaId);
}

