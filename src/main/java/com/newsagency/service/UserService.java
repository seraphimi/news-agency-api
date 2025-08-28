package com.newsagency.service;

import com.newsagency.dto.UserDTO;
import com.newsagency.entity.User;
import com.newsagency.exception.ResourceNotFoundException;
import com.newsagency.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for User entity operations.
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new user.
     *
     * @param userDTO the user data
     * @return created user DTO
     */
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating new user with username: {}", userDTO.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        
        logger.info("User created successfully with ID: {}", savedUser.getId());
        return convertToDTO(savedUser);
    }

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return user DTO
     */
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        return convertToDTO(user);
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable pagination information
     * @return page of user DTOs
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        logger.debug("Fetching all users with pagination");
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDTO);
    }

    /**
     * Update user.
     *
     * @param id the user ID
     * @param userDTO the updated user data
     * @return updated user DTO
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with ID: {}", id);
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Check if new username conflicts with another user
        if (!existingUser.getUsername().equals(userDTO.getUsername()) && 
            userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }

        // Check if new email conflicts with another user
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
            userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        
        // Only update password if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword());
        }

        User updatedUser = userRepository.save(existingUser);
        
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        return convertToDTO(updatedUser);
    }

    /**
     * Delete user by ID.
     *
     * @param id the user ID
     */
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }

    /**
     * Search users by first name.
     *
     * @param firstName the first name to search for
     * @return list of user DTOs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchByFirstName(String firstName) {
        logger.debug("Searching users by first name: {}", firstName);
        
        List<User> users = userRepository.findByFirstNameContainingIgnoreCase(firstName);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search users by last name.
     *
     * @param lastName the last name to search for
     * @return list of user DTOs
     */
    @Transactional(readOnly = true)
    public List<UserDTO> searchByLastName(String lastName) {
        logger.debug("Searching users by last name: {}", lastName);
        
        List<User> users = userRepository.findByLastNameContainingIgnoreCase(lastName);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert User entity to UserDTO.
     *
     * @param user the user entity
     * @return user DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        // Don't include password in DTO for security
        return userDTO;
    }

    /**
     * Convert UserDTO to User entity.
     *
     * @param userDTO the user DTO
     * @return user entity
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // In real app, this should be hashed
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        return user;
    }
}