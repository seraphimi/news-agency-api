package com.newsagency.repository;

import com.newsagency.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Author entity.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find author by name.
     *
     * @param name the name to search for
     * @return Optional containing the author if found
     */
    Optional<Author> findByName(String name);

    /**
     * Find author by email.
     *
     * @param email the email to search for
     * @return Optional containing the author if found
     */
    Optional<Author> findByEmail(String email);

    /**
     * Check if email exists.
     *
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find authors by name containing the given string (case-insensitive).
     *
     * @param name the name to search for
     * @return list of authors matching the criteria
     */
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find authors by specialization containing the given string (case-insensitive).
     *
     * @param specialization the specialization to search for
     * @return list of authors matching the criteria
     */
    @Query("SELECT a FROM Author a WHERE LOWER(a.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))")
    List<Author> findBySpecializationContainingIgnoreCase(@Param("specialization") String specialization);

    /**
     * Find authors who have published news articles.
     *
     * @return list of authors with published articles
     */
    @Query("SELECT DISTINCT a FROM Author a JOIN a.newsArticles n WHERE n.published = true")
    List<Author> findAuthorsWithPublishedNews();
}