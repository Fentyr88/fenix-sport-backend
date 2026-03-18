package com.fenix_sport.back.infrastructure.config;

import com.fenix_sport.back.application.port.input.AuthUseCase;
import com.fenix_sport.back.application.port.input.CategoryUseCase;
import com.fenix_sport.back.application.port.input.ProductUseCase;
import com.fenix_sport.back.application.port.output.*;
import com.fenix_sport.back.application.service.AuthService;
import com.fenix_sport.back.application.service.CategoryService;
import com.fenix_sport.back.application.service.ProductService;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.adapter.*;
import com.fenix_sport.back.infrastructure.adapter.output.persistence.repository.*;
import com.fenix_sport.back.infrastructure.adapter.output.security.BcryptPasswordEncoderAdapter;
import com.fenix_sport.back.infrastructure.adapter.output.security.JwtTokenAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class BeanConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PasswordEncoderPort passwordEncoderPort(PasswordEncoder passwordEncoder) {
		return new BcryptPasswordEncoderAdapter(passwordEncoder);
	}

	@Bean
	public JwtTokenPort jwtTokenPort(AppProperties props) {
		return new JwtTokenAdapter(props.getJwt().getSecret(), props.getJwt().getExpirationMinutes());
	}

	@Bean
	public UserRepositoryPort userRepositoryPort(JpaUserRepository jpaUserRepository) {
		return new UserPersistenceAdapter(jpaUserRepository);
	}

	@Bean
	public RoleRepositoryPort roleRepositoryPort(JpaRoleRepository jpaRoleRepository) {
		return new RolePersistenceAdapter(jpaRoleRepository);
	}

	@Bean
	public CategoryRepositoryPort categoryRepositoryPort(JpaCategoryRepository jpaCategoryRepository,
	                                                   JpaProductRepository jpaProductRepository) {
		return new CategoryPersistenceAdapter(jpaCategoryRepository, jpaProductRepository);
	}

	@Bean
	public ProductRepositoryPort productRepositoryPort(JpaProductRepository jpaProductRepository) {
		return new ProductPersistenceAdapter(jpaProductRepository);
	}

	@Bean
	public AuthUseCase authUseCase(UserRepositoryPort userRepositoryPort,
	                              RoleRepositoryPort roleRepositoryPort,
	                              PasswordEncoderPort passwordEncoderPort,
	                              JwtTokenPort jwtTokenPort) {
		return new AuthService(userRepositoryPort, roleRepositoryPort, passwordEncoderPort, jwtTokenPort);
	}

	@Bean
	public CategoryUseCase categoryUseCase(CategoryRepositoryPort categoryRepositoryPort) {
		return new CategoryService(categoryRepositoryPort);
	}

	@Bean
	public ProductUseCase productUseCase(ProductRepositoryPort productRepositoryPort,
	                                    CategoryRepositoryPort categoryRepositoryPort) {
		return new ProductService(productRepositoryPort, categoryRepositoryPort);
	}
}

