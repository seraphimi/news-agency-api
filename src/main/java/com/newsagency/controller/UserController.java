package com.newsagency.controller;

import com.newsagency.dto.UserDTO;
import com.newsagency.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User management operations.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Create a new user.
     *
     * @param userDTO the user data
     * @return ResponseEntity with created user
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        logger.info("REST request to create user: {}", userDTO.getUsername());
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return ResponseEntity with user data
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        logger.debug("REST request to get user by ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users with pagination.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of users
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        logger.debug("REST request to get all users");
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    /**
     * Update user.
     *
     * @param id the user ID
     * @param userDTO the updated user data
     * @return ResponseEntity with updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, 
                                             @Valid @RequestBody UserDTO userDTO) {
        logger.info("REST request to update user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete user by ID.
     *
     * @param id the user ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("REST request to delete user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search users by first name.
     *
     * @param firstName the first name to search for
     * @return ResponseEntity with list of users
     */
    @GetMapping("/search/firstname/{firstName}")
    public ResponseEntity<List<UserDTO>> searchByFirstName(@PathVariable String firstName) {
        logger.debug("REST request to search users by first name: {}", firstName);
        List<UserDTO> users = userService.searchByFirstName(firstName);
        return ResponseEntity.ok(users);
    }

    /**
     * Search users by last name.
     *
     * @param lastName the last name to search for
     * @return ResponseEntity with list of users
     */
    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<List<UserDTO>> searchByLastName(@PathVariable String lastName) {
        logger.debug("REST request to search users by last name: {}", lastName);
        List<UserDTO> users = userService.searchByLastName(lastName);
        return ResponseEntity.ok(users);
    }
}