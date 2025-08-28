package com.newsagency.service;

import com.newsagency.dto.AuthorDTO;
import com.newsagency.entity.Author;
import com.newsagency.exception.ResourceNotFoundException;
import com.newsagency.repository.AuthorRepository;
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
 * Service class for Author entity operations.
 */
@Service
@Transactional
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    @Autowired
    private AuthorRepository authorRepository;

    /**
     * Create a new author.
     *
     * @param authorDTO the author data
     * @return created author DTO
     */
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        logger.info("Creating new author with name: {}", authorDTO.getName());
        
        // Check if email already exists (if provided)
        if (authorDTO.getEmail() != null && authorRepository.existsByEmail(authorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + authorDTO.getEmail());
        }

        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        
        logger.info("Author created successfully with ID: {}", savedAuthor.getId());
        return convertToDTO(savedAuthor);
    }

    /**
     * Get author by ID.
     *
     * @param id the author ID
     * @return author DTO
     */
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        logger.debug("Fetching author with ID: {}", id);
        
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
        
        return convertToDTO(author);
    }

    /**
     * Get all authors with pagination.
     *
     * @param pageable pagination information
     * @return page of author DTOs
     */
    @Transactional(readOnly = true)
    public Page<AuthorDTO> getAllAuthors(Pageable pageable) {
        logger.debug("Fetching all authors with pagination");
        
        Page<Author> authors = authorRepository.findAll(pageable);
        return authors.map(this::convertToDTO);
    }

    /**
     * Update author.
     *
     * @param id the author ID
     * @param authorDTO the updated author data
     * @return updated author DTO
     */
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        logger.info("Updating author with ID: {}", id);
        
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));

        // Check if new email conflicts with another author
        if (authorDTO.getEmail() != null && 
            !authorDTO.getEmail().equals(existingAuthor.getEmail()) && 
            authorRepository.existsByEmail(authorDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + authorDTO.getEmail());
        }

        existingAuthor.setName(authorDTO.getName());
        existingAuthor.setEmail(authorDTO.getEmail());
        existingAuthor.setPhoneNumber(authorDTO.getPhoneNumber());
        existingAuthor.setBio(authorDTO.getBio());
        existingAuthor.setSpecialization(authorDTO.getSpecialization());

        Author updatedAuthor = authorRepository.save(existingAuthor);
        
        logger.info("Author updated successfully with ID: {}", updatedAuthor.getId());
        return convertToDTO(updatedAuthor);
    }

    /**
     * Delete author by ID.
     *
     * @param id the author ID
     */
    public void deleteAuthor(Long id) {
        logger.info("Deleting author with ID: {}", id);
        
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with ID: " + id);
        }

        authorRepository.deleteById(id);
        logger.info("Author deleted successfully with ID: {}", id);
    }

    /**
     * Search authors by name.
     *
     * @param name the name to search for
     * @return list of author DTOs
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> searchByName(String name) {
        logger.debug("Searching authors by name: {}", name);
        
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(name);
        return authors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search authors by specialization.
     *
     * @param specialization the specialization to search for
     * @return list of author DTOs
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> searchBySpecialization(String specialization) {
        logger.debug("Searching authors by specialization: {}", specialization);
        
        List<Author> authors = authorRepository.findBySpecializationContainingIgnoreCase(specialization);
        return authors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get authors with published news.
     *
     * @return list of author DTOs with published news
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAuthorsWithPublishedNews() {
        logger.debug("Fetching authors with published news");
        
        List<Author> authors = authorRepository.findAuthorsWithPublishedNews();
        return authors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Author entity to AuthorDTO.
     *
     * @param author the author entity
     * @return author DTO
     */
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setEmail(author.getEmail());
        authorDTO.setPhoneNumber(author.getPhoneNumber());
        authorDTO.setBio(author.getBio());
        authorDTO.setSpecialization(author.getSpecialization());
        authorDTO.setCreatedAt(author.getCreatedAt());
        authorDTO.setUpdatedAt(author.getUpdatedAt());
        return authorDTO;
    }

    /**
     * Convert AuthorDTO to Author entity.
     *
     * @param authorDTO the author DTO
     * @return author entity
     */
    private Author convertToEntity(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setEmail(authorDTO.getEmail());
        author.setPhoneNumber(authorDTO.getPhoneNumber());
        author.setBio(authorDTO.getBio());
        author.setSpecialization(authorDTO.getSpecialization());
        return author;
    }
}