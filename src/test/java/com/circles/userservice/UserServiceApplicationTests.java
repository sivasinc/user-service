package com.circles.userservice;

import com.circles.userservice.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
		// Test that application context loads successfully with H2

	}
}
