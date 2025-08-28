package com.newsagency.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Author entity.
 */
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    @Size(max = 200, message = "Specialization cannot exceed 200 characters")
    private String specialization;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public AuthorDTO() {}

    public AuthorDTO(String name, String email, String phoneNumber, String bio, String specialization) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.specialization = specialization;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}