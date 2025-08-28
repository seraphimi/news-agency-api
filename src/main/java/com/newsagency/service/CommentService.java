package com.newsagency.service;

import com.newsagency.dto.CommentDTO;
import com.newsagency.entity.Comment;
import com.newsagency.entity.News;
import com.newsagency.entity.User;
import com.newsagency.exception.ResourceNotFoundException;
import com.newsagency.repository.CommentRepository;
import com.newsagency.repository.NewsRepository;
import com.newsagency.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Comment entity operations.
 */
@Service
@Transactional
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NewsRepository newsRepository;
    
    @Autowired
    private NotificationService notificationService;

    /**
     * Create a new comment.
     *
     * @param commentDTO the comment data
     * @return created comment DTO
     */
    public CommentDTO createComment(CommentDTO commentDTO) {
        logger.info("Creating new comment for news ID: {} by user ID: {}", 
                   commentDTO.getNewsId(), commentDTO.getUserId());
        
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + commentDTO.getUserId()));
        
        News news = newsRepository.findById(commentDTO.getNewsId())
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + commentDTO.getNewsId()));

        Comment comment = convertToEntity(commentDTO, user, news);
        Comment savedComment = commentRepository.save(comment);
        
        // Send notification asynchronously
        notificationService.notifyNewComment(savedComment);
        
        logger.info("Comment created successfully with ID: {}", savedComment.getId());
        return convertToDTO(savedComment);
    }

    /**
     * Get comment by ID.
     *
     * @param id the comment ID
     * @return comment DTO
     */
    @Transactional(readOnly = true)
    public CommentDTO getCommentById(Long id) {
        logger.debug("Fetching comment with ID: {}", id);
        
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + id));
        
        return convertToDTO(comment);
    }

    /**
     * Get all comments with pagination.
     *
     * @param pageable pagination information
     * @return page of comment DTOs
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> getAllComments(Pageable pageable) {
        logger.debug("Fetching all comments with pagination");
        
        Page<Comment> comments = commentRepository.findAll(pageable);
        return comments.map(this::convertToDTO);
    }

    /**
     * Update comment.
     *
     * @param id the comment ID
     * @param commentDTO the updated comment data
     * @return updated comment DTO
     */
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        logger.info("Updating comment with ID: {}", id);
        
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with ID: " + id));

        existingComment.setContent(commentDTO.getContent());

        Comment updatedComment = commentRepository.save(existingComment);
        
        logger.info("Comment updated successfully with ID: {}", updatedComment.getId());
        return convertToDTO(updatedComment);
    }

    /**
     * Delete comment by ID.
     *
     * @param id the comment ID
     */
    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with ID: " + id);
        }

        commentRepository.deleteById(id);
        logger.info("Comment deleted successfully with ID: {}", id);
    }

    /**
     * Get comments by news ID.
     *
     * @param newsId the news ID
     * @param pageable pagination information
     * @return page of comment DTOs for the news article
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByNews(Long newsId, Pageable pageable) {
        logger.debug("Fetching comments for news ID: {}", newsId);
        
        Page<Comment> comments = commentRepository.findByNewsIdOrderByCreatedAtDesc(newsId, pageable);
        return comments.map(this::convertToDTO);
    }

    /**
     * Get comments by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of comment DTOs by the user
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByUser(Long userId, Pageable pageable) {
        logger.debug("Fetching comments for user ID: {}", userId);
        
        Page<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return comments.map(this::convertToDTO);
    }

    /**
     * Search comments by content.
     *
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return page of comment DTOs containing the keyword
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> searchByContent(String keyword, Pageable pageable) {
        logger.debug("Searching comments by keyword: {}", keyword);
        
        Page<Comment> comments = commentRepository.findByContentContainingIgnoreCase(keyword, pageable);
        return comments.map(this::convertToDTO);
    }

    /**
     * Get recent comments for a news article.
     *
     * @param newsId the news ID
     * @param hours number of hours to look back
     * @return list of recent comment DTOs
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getRecentCommentsByNews(Long newsId, int hours) {
        logger.debug("Fetching recent comments for news ID: {} from last {} hours", newsId, hours);
        
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Comment> comments = commentRepository.findRecentCommentsByNews(newsId, since);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count comments by news ID.
     *
     * @param newsId the news ID
     * @return count of comments for the news article
     */
    @Transactional(readOnly = true)
    public long countCommentsByNews(Long newsId) {
        logger.debug("Counting comments for news ID: {}", newsId);
        return commentRepository.countByNewsId(newsId);
    }

    /**
     * Count comments by user ID.
     *
     * @param userId the user ID
     * @return count of comments by the user
     */
    @Transactional(readOnly = true)
    public long countCommentsByUser(Long userId) {
        logger.debug("Counting comments for user ID: {}", userId);
        return commentRepository.countByUserId(userId);
    }

    /**
     * Get comments created after a specific date.
     *
     * @param date the date to search from
     * @return list of comment DTOs created after the date
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsAfterDate(LocalDateTime date) {
        logger.debug("Fetching comments created after: {}", date);
        
        List<Comment> comments = commentRepository.findCommentsAfterDate(date);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Comment entity to CommentDTO.
     *
     * @param comment the comment entity
     * @return comment DTO
     */
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setUsername(comment.getUser().getUsername());
        commentDTO.setNewsId(comment.getNews().getId());
        commentDTO.setNewsTitle(comment.getNews().getTitle());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());
        return commentDTO;
    }

    /**
     * Convert CommentDTO to Comment entity.
     *
     * @param commentDTO the comment DTO
     * @param user the user entity
     * @param news the news entity
     * @return comment entity
     */
    private Comment convertToEntity(CommentDTO commentDTO, User user, News news) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUser(user);
        comment.setNews(news);
        return comment;
    }
}