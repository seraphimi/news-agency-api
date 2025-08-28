package com.newsagency.controller;

import com.newsagency.dto.AuthorDTO;
import com.newsagency.service.AuthorService;
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
 * REST Controller for Author management operations.
 */
@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorService authorService;

    /**
     * Create a new author.
     *
     * @param authorDTO the author data
     * @return ResponseEntity with created author
     */
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        logger.info("REST request to create author: {}", authorDTO.getName());
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    /**
     * Get author by ID.
     *
     * @param id the author ID
     * @return ResponseEntity with author data
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        logger.debug("REST request to get author by ID: {}", id);
        AuthorDTO author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    /**
     * Get all authors with pagination.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of authors
     */
    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> getAllAuthors(Pageable pageable) {
        logger.debug("REST request to get all authors");
        Page<AuthorDTO> authors = authorService.getAllAuthors(pageable);
        return ResponseEntity.ok(authors);
    }

    /**
     * Update author.
     *
     * @param id the author ID
     * @param authorDTO the updated author data
     * @return ResponseEntity with updated author
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, 
                                                 @Valid @RequestBody AuthorDTO authorDTO) {
        logger.info("REST request to update author with ID: {}", id);
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return ResponseEntity.ok(updatedAuthor);
    }

    /**
     * Delete author by ID.
     *
     * @param id the author ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        logger.info("REST request to delete author with ID: {}", id);
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search authors by name.
     *
     * @param name the name to search for
     * @return ResponseEntity with list of authors
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<AuthorDTO>> searchByName(@PathVariable String name) {
        logger.debug("REST request to search authors by name: {}", name);
        List<AuthorDTO> authors = authorService.searchByName(name);
        return ResponseEntity.ok(authors);
    }

    /**
     * Search authors by specialization.
     *
     * @param specialization the specialization to search for
     * @return ResponseEntity with list of authors
     */
    @GetMapping("/search/specialization/{specialization}")
    public ResponseEntity<List<AuthorDTO>> searchBySpecialization(@PathVariable String specialization) {
        logger.debug("REST request to search authors by specialization: {}", specialization);
        List<AuthorDTO> authors = authorService.searchBySpecialization(specialization);
        return ResponseEntity.ok(authors);
    }

    /**
     * Get authors with published news.
     *
     * @return ResponseEntity with list of authors with published news
     */
    @GetMapping("/published")
    public ResponseEntity<List<AuthorDTO>> getAuthorsWithPublishedNews() {
        logger.debug("REST request to get authors with published news");
        List<AuthorDTO> authors = authorService.getAuthorsWithPublishedNews();
        return ResponseEntity.ok(authors);
    }
}