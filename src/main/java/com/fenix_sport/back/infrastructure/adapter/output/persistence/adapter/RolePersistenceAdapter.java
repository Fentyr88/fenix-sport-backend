package com.fenix_sport.back.infrastructure.adapter.output.persistence.adapter;

import com.fenix_sport.back.application.port.output.RoleRepositoryPort;
import com.fenix_sport.back.domain.model.Role;
import com.fenix_sport.back.domain.model.RoleName;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.mapper.RoleMapper;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaRoleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class RolePersistenceAdapter implements RoleRepositoryPort {
	private final JpaRoleRepository roleRepository;

	public RolePersistenceAdapter(JpaRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Optional<Role> findByName(RoleName name) {
		return roleRepository.findByName(name).map(RoleMapper::toDomain);
	}

	@Override
	@Transactional
	public Role save(Role role) {
		return RoleMapper.toDomain(roleRepository.save(RoleMapper.toEntity(role)));
	}
}

