package com.newsagency.repository;

import com.newsagency.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name.
     *
     * @param name the name to search for
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);

    /**
     * Check if name exists.
     *
     * @param name the name to check
     * @return true if name exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Find categories by name containing the given string (case-insensitive).
     *
     * @param name the name to search for
     * @return list of categories matching the criteria
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find categories that have published news articles.
     *
     * @return list of categories with published articles
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.newsArticles n WHERE n.published = true")
    List<Category> findCategoriesWithPublishedNews();

    /**
     * Find categories with news count.
     *
     * @return list of categories ordered by news count (descending)
     */
    @Query("SELECT c FROM Category c LEFT JOIN c.newsArticles n GROUP BY c ORDER BY COUNT(n) DESC")
    List<Category> findCategoriesOrderByNewsCount();
}