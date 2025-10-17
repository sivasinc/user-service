package com.circles.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
		// Test that JPA context loads successfully with H2
	}
}
