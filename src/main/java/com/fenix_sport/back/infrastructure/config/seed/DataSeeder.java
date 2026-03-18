package com.fenix_sport.back.infrastructure.config.seed;

import com.fenix_sport.back.domain.model.RoleName;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.RoleEntity;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaRoleRepository;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.JpaUserRepository;
import com.fenix_sport.back.infrastructure.config.AppProperties;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataSeeder implements ApplicationRunner {
	private final JpaRoleRepository roleRepository;
	private final JpaUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AppProperties props;

	public DataSeeder(JpaRoleRepository roleRepository,
	                  JpaUserRepository userRepository,
	                  PasswordEncoder passwordEncoder,
	                  AppProperties props) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.props = props;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		ensureRole(RoleName.USER);
		RoleEntity adminRole = ensureRole(RoleName.ADMIN);

		if (!props.getSeed().getAdmin().isEnabled()) return;

		String email = props.getSeed().getAdmin().getEmail();
		if (email == null || email.isBlank()) return;

		if (userRepository.existsByEmailIgnoreCase(email)) return;

		UserEntity admin = new UserEntity();
		admin.setNombre(props.getSeed().getAdmin().getNombre() == null ? "Admin" : props.getSeed().getAdmin().getNombre());
		admin.setEmail(email);
		admin.setPassword(passwordEncoder.encode(props.getSeed().getAdmin().getPassword()));
		admin.setActivo(true);
		admin.setFechaRegistro(LocalDate.now());
		admin.setRoles(Set.of(adminRole));
		userRepository.save(admin);
	}

	private RoleEntity ensureRole(RoleName name) {
		return roleRepository.findByName(name).orElseGet(() -> {
			RoleEntity r = new RoleEntity();
			r.setName(name);
			return roleRepository.save(r);
		});
	}
}

