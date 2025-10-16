package com.circles.userservice.service;

import com.circles.userservice.dto.CreateUserRequest;
import com.circles.userservice.dto.UserDTO;
import com.circles.userservice.exception.DuplicateResourceException;
import com.circles.userservice.exception.ResourceNotFoundException;
import com.circles.userservice.model.User;
import com.circles.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .active(true)
                .build();

        createUserRequest = CreateUserRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_ShouldCreateUser_WhenDataIsValid() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO result = userService.createUser(createUserRequest);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.createUser(createUserRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}