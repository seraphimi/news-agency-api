package com.newsagency.controller;

import com.newsagency.dto.CommentDTO;
import com.newsagency.service.CommentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Comment management operations.
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    /**
     * Create a new comment.
     *
     * @param commentDTO the comment data
     * @return ResponseEntity with created comment
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        logger.info("REST request to create comment for news ID: {} by user ID: {}", 
                   commentDTO.getNewsId(), commentDTO.getUserId());
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * Get comment by ID.
     *
     * @param id the comment ID
     * @return ResponseEntity with comment data
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        logger.debug("REST request to get comment by ID: {}", id);
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    /**
     * Get all comments with pagination.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of comments
     */
    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getAllComments(Pageable pageable) {
        logger.debug("REST request to get all comments");
        Page<CommentDTO> comments = commentService.getAllComments(pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Update comment.
     *
     * @param id the comment ID
     * @param commentDTO the updated comment data
     * @return ResponseEntity with updated comment
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, 
                                                   @Valid @RequestBody CommentDTO commentDTO) {
        logger.info("REST request to update comment with ID: {}", id);
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * Delete comment by ID.
     *
     * @param id the comment ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        logger.info("REST request to delete comment with ID: {}", id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get comments by news ID.
     *
     * @param newsId the news ID
     * @param pageable pagination parameters
     * @return ResponseEntity with page of comments for the news article
     */
    @GetMapping("/news/{newsId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByNews(@PathVariable Long newsId, 
                                                             Pageable pageable) {
        logger.debug("REST request to get comments for news ID: {}", newsId);
        Page<CommentDTO> comments = commentService.getCommentsByNews(newsId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get comments by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return ResponseEntity with page of comments by the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByUser(@PathVariable Long userId, 
                                                             Pageable pageable) {
        logger.debug("REST request to get comments for user ID: {}", userId);
        Page<CommentDTO> comments = commentService.getCommentsByUser(userId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Search comments by content.
     *
     * @param keyword the keyword to search for
     * @param pageable pagination parameters
     * @return ResponseEntity with page of comments containing the keyword
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CommentDTO>> searchByContent(@RequestParam String keyword, 
                                                           Pageable pageable) {
        logger.debug("REST request to search comments by keyword: {}", keyword);
        Page<CommentDTO> comments = commentService.searchByContent(keyword, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get recent comments for a news article.
     *
     * @param newsId the news ID
     * @param hours number of hours to look back (default: 24)
     * @return ResponseEntity with list of recent comments
     */
    @GetMapping("/news/{newsId}/recent")
    public ResponseEntity<List<CommentDTO>> getRecentCommentsByNews(@PathVariable Long newsId, 
                                                                   @RequestParam(defaultValue = "24") int hours) {
        logger.debug("REST request to get recent comments for news ID: {} from last {} hours", newsId, hours);
        List<CommentDTO> comments = commentService.getRecentCommentsByNews(newsId, hours);
        return ResponseEntity.ok(comments);
    }

    /**
     * Count comments by news ID.
     *
     * @param newsId the news ID
     * @return ResponseEntity with comment count
     */
    @GetMapping("/news/{newsId}/count")
    public ResponseEntity<Long> countCommentsByNews(@PathVariable Long newsId) {
        logger.debug("REST request to count comments for news ID: {}", newsId);
        long count = commentService.countCommentsByNews(newsId);
        return ResponseEntity.ok(count);
    }

    /**
     * Count comments by user ID.
     *
     * @param userId the user ID
     * @return ResponseEntity with comment count
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countCommentsByUser(@PathVariable Long userId) {
        logger.debug("REST request to count comments for user ID: {}", userId);
        long count = commentService.countCommentsByUser(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get comments created after a specific date.
     *
     * @param date the date to search from (ISO format)
     * @return ResponseEntity with list of comments created after the date
     */
    @GetMapping("/after/{date}")
    public ResponseEntity<List<CommentDTO>> getCommentsAfterDate(@PathVariable String date) {
        logger.debug("REST request to get comments created after: {}", date);
        LocalDateTime dateTime = LocalDateTime.parse(date);
        List<CommentDTO> comments = commentService.getCommentsAfterDate(dateTime);
        return ResponseEntity.ok(comments);
    }
}