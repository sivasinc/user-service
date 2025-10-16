package com.circles.userservice.service;

import com.circles.userservice.dto.CreateUserRequest;
import com.circles.userservice.dto.UpdateUserRequest;
import com.circles.userservice.dto.UserDTO;
import com.circles.userservice.exception.DuplicateResourceException;
import com.circles.userservice.exception.ResourceNotFoundException;
import com.circles.userservice.model.User;
import com.circles.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        log.info("Creating new user with username: {}", request.getUsername());

        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with id: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update fields if provided
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", updatedUser.getId());
        return convertToDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> searchUsersByFirstName(String firstName) {
        log.info("Searching users by first name: {}", firstName);
        return userRepository.findByFirstNameContainingIgnoreCase(firstName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Entity to DTO
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}