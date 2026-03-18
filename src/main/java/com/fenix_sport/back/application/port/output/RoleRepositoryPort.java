package com.fenix_sport.back.application.port.output;

import com.fenix_sport.back.domain.model.Role;
import com.fenix_sport.back.domain.model.RoleName;

import java.util.Optional;

public interface RoleRepositoryPort {
	Optional<Role> findByName(RoleName name);

	Role save(Role role);
}

