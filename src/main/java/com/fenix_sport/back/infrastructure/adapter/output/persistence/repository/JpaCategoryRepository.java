package com.fenix_sport.back.infrastructure.adapter.output.persistence.repository;

import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, Long> {
	boolean existsByNombreIgnoreCase(String nombre);
}

