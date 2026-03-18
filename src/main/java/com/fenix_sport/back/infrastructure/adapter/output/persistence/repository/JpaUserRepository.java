package com.fenix_sport.back.infrastructure.adapter.output.persistence.repository;

import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByEmailIgnoreCase(String email);

	Optional<UserEntity> findByEmailIgnoreCase(String email);
}

