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
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Clean up database before each test
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

        ResponseEntity<UserDTO> response =
                restTemplate.postForEntity("/api/users", request, UserDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("john.doe", response.getBody().getUsername());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        // Create a user first
        CreateUserRequest request = CreateUserRequest.builder()
                .username("jane.doe")
                .email("jane@example.com")
                .password("password456")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        ResponseEntity<UserDTO> createdResponse =
                restTemplate.postForEntity("/api/users", request, UserDTO.class);

        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        Long userId = createdResponse.getBody().getId();

        // Fetch by ID
        ResponseEntity<UserDTO> response =
                restTemplate.getForEntity("/api/users/" + userId, UserDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jane.doe", response.getBody().getUsername());
    }
}
