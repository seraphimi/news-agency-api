package com.newsagency.repository;

import com.newsagency.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for News entity.
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find published news articles.
     *
     * @param pageable pagination information
     * @return page of published news articles
     */
    Page<News> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);

    /**
     * Find news by author ID.
     *
     * @param authorId the author ID
     * @param pageable pagination information
     * @return page of news articles by the author
     */
    Page<News> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    /**
     * Find news by category ID.
     *
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of news articles in the category
     */
    Page<News> findByCategoryIdOrderByCreatedAtDesc(Long categoryId, Pageable pageable);

    /**
     * Search news by keyword in title or content (case-insensitive).
     *
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return page of news articles matching the keyword
     */
    @Query("SELECT n FROM News n WHERE n.published = true AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.summary) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<News> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find top news by view count.
     *
     * @param pageable pagination information
     * @return page of news articles ordered by view count (descending)
     */
    @Query("SELECT n FROM News n WHERE n.published = true ORDER BY n.viewCount DESC")
    Page<News> findTopNewsByViewCount(Pageable pageable);

    /**
     * Find recent published news.
     *
     * @param since the date since when to find news
     * @param pageable pagination information
     * @return page of recent published news articles
     */
    @Query("SELECT n FROM News n WHERE n.published = true AND n.publishedAt >= :since ORDER BY n.publishedAt DESC")
    Page<News> findRecentNews(@Param("since") LocalDateTime since, Pageable pageable);

    /**
     * Count published news by author.
     *
     * @param authorId the author ID
     * @return count of published news by the author
     */
    @Query("SELECT COUNT(n) FROM News n WHERE n.author.id = :authorId AND n.published = true")
    long countPublishedNewsByAuthor(@Param("authorId") Long authorId);

    /**
     * Count published news by category.
     *
     * @param categoryId the category ID
     * @return count of published news in the category
     */
    @Query("SELECT COUNT(n) FROM News n WHERE n.category.id = :categoryId AND n.published = true")
    long countPublishedNewsByCategory(@Param("categoryId") Long categoryId);

    /**
     * Increment view count for a news article.
     *
     * @param newsId the news ID
     */
    @Modifying
    @Query("UPDATE News n SET n.viewCount = n.viewCount + 1 WHERE n.id = :newsId")
    void incrementViewCount(@Param("newsId") Long newsId);

    /**
     * Find news by title containing keyword (case-insensitive).
     *
     * @param title the title keyword
     * @return list of news articles with matching titles
     */
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<News> findByTitleContainingIgnoreCase(@Param("title") String title);
}