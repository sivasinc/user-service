package com.circles.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void contextLoads() {
		// Verify that JPA context loads successfully
		assertNotNull(entityManager);
	}
}
