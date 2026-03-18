package com.fenix_sport.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = {
		// En tests no dependemos de MySQL local; usamos H2 en memoria.
		"spring.datasource.url=jdbc:h2:mem:fenix_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		// En tests dejamos que Hibernate cree el esquema con las @Entity.
		"spring.jpa.hibernate.ddl-auto=create-drop",
		// Evita depender de metadata JDBC para determinar dialect.
		"spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
