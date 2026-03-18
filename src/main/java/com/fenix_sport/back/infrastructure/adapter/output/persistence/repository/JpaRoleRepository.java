package com.fenix_sport.back.infrastructure.adapter.output.persistence.repository;

import com.fenix_sport.back.domain.model.RoleName;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(RoleName name);
}

