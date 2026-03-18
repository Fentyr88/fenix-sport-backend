package com.fenix_sport.back.infrastructure.adapter.output.persistence.entity;

import com.fenix_sport.back.domain.model.RoleName;
import jakarta.persistence.*;

@Entity
@Table(name = "rol")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "nombre", nullable = false, unique = true, length = 50)
	private RoleName name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}
}

