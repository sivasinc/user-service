package com.circles.userservice.integration;

import com.circles.userservice.dto.CreateUserRequest;
import com.circles.userservice.dto.UserDTO;
import com.circles.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("john.doe")
                .email("john@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "/api/users",
                request,
                UserDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("john.doe", response.getBody().getUsername());
        assertEquals("john@example.com", response.getBody().getEmail());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        // Create test user
        CreateUserRequest request = CreateUserRequest.builder()
                .username("jane.doe")
                .email("jane@example.com")
                .password("password123")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        restTemplate.postForEntity("/api/users", request, UserDTO.class);

        // Get all users
        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(
                "/api/users",
                UserDTO[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        // Create user
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test.user")
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();

        ResponseEntity<UserDTO> createResponse = restTemplate.postForEntity(
                "/api/users",
                request,
                UserDTO.class
        );

        Long userId = createResponse.getBody().getId();

        // Get user by ID
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                "/api/users/" + userId,
                UserDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userId, response.getBody().getId());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        // Create user
        CreateUserRequest request = CreateUserRequest.builder()
                .username("delete.user")
                .email("delete@example.com")
                .password("password123")
                .firstName("Delete")
                .lastName("User")
                .build();

        ResponseEntity<UserDTO> createResponse = restTemplate.postForEntity(
                "/api/users",
                request,
                UserDTO.class
        );

        Long userId = createResponse.getBody().getId();

        // Delete user
        restTemplate.delete("/api/users/" + userId);

        // Verify deleted
        ResponseEntity<UserDTO> getResponse = restTemplate.getForEntity(
                "/api/users/" + userId,
                UserDTO.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}