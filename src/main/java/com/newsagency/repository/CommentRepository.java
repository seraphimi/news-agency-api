package com.newsagency.repository;

import com.newsagency.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Comment entity.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find comments by news ID.
     *
     * @param newsId the news ID
     * @param pageable pagination information
     * @return page of comments for the news article
     */
    Page<Comment> findByNewsIdOrderByCreatedAtDesc(Long newsId, Pageable pageable);

    /**
     * Find comments by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of comments by the user
     */
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find recent comments for a news article.
     *
     * @param newsId the news ID
     * @param since the date since when to find comments
     * @return list of recent comments
     */
    @Query("SELECT c FROM Comment c WHERE c.news.id = :newsId AND c.createdAt >= :since ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsByNews(@Param("newsId") Long newsId, @Param("since") LocalDateTime since);

    /**
     * Count comments by news ID.
     *
     * @param newsId the news ID
     * @return count of comments for the news article
     */
    long countByNewsId(Long newsId);

    /**
     * Count comments by user ID.
     *
     * @param userId the user ID
     * @return count of comments by the user
     */
    long countByUserId(Long userId);

    /**
     * Find comments by content containing keyword (case-insensitive).
     *
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return page of comments containing the keyword
     */
    @Query("SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY c.createdAt DESC")
    Page<Comment> findByContentContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Find comments created after a specific date.
     *
     * @param date the date to search from
     * @return list of comments created after the date
     */
    @Query("SELECT c FROM Comment c WHERE c.createdAt > :date ORDER BY c.createdAt DESC")
    List<Comment> findCommentsAfterDate(@Param("date") LocalDateTime date);

    /**
     * Find latest comments for notification purposes.
     *
     * @param limit the maximum number of comments to return
     * @return list of latest comments
     */
    @Query(value = "SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findLatestComments(Pageable pageable);
}