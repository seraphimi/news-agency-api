package com.newsagency.service;

import com.newsagency.entity.Comment;
import com.newsagency.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for handling notifications with multithreading support.
 * 
 * This service provides asynchronous notification functionality for
 * comment-related events and scheduled tasks for monitoring new comments.
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private CommentRepository commentRepository;

    private LocalDateTime lastCheckedTime = LocalDateTime.now();

    /**
     * Send notification for a new comment asynchronously.
     * 
     * @param comment the new comment
     * @return CompletableFuture for async processing
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> notifyNewComment(Comment comment) {
        try {
            logger.info("Processing notification for new comment ID: {} on news: '{}' by user: '{}'", 
                       comment.getId(), 
                       comment.getNews().getTitle(), 
                       comment.getUser().getUsername());

            // Simulate notification processing (e.g., sending email, push notification, etc.)
            Thread.sleep(1000); // Simulate processing time

            // In a real application, this would:
            // 1. Send email to the news author
            // 2. Send push notifications to subscribers
            // 3. Update notification queues
            // 4. Log notification events

            logger.info("Notification sent successfully for comment ID: {}", comment.getId());
            
            // Notify author about the new comment
            notifyAuthor(comment);
            
            // Notify other commenters on the same news article
            notifyOtherCommenters(comment);

        } catch (InterruptedException e) {
            logger.error("Notification processing interrupted for comment ID: {}", comment.getId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error processing notification for comment ID: {}", comment.getId(), e);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Notify the author of the news article about a new comment.
     * 
     * @param comment the new comment
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> notifyAuthor(Comment comment) {
        try {
            logger.info("Notifying author '{}' about new comment on article: '{}'", 
                       comment.getNews().getAuthor().getName(), 
                       comment.getNews().getTitle());

            // Simulate author notification processing
            Thread.sleep(500);

            // In a real application, this would send an email to the author
            logger.info("Author notification sent successfully to: {}", 
                       comment.getNews().getAuthor().getName());

        } catch (InterruptedException e) {
            logger.error("Author notification interrupted for comment ID: {}", comment.getId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error sending author notification for comment ID: {}", comment.getId(), e);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Notify other commenters on the same news article about a new comment.
     * 
     * @param comment the new comment
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> notifyOtherCommenters(Comment comment) {
        try {
            logger.info("Notifying other commenters about new comment on article: '{}'", 
                       comment.getNews().getTitle());

            // Get recent comments from other users on the same news article
            LocalDateTime since = LocalDateTime.now().minusDays(7); // Last 7 days
            List<Comment> recentComments = commentRepository
                    .findRecentCommentsByNews(comment.getNews().getId(), since);

            int notificationCount = 0;
            for (Comment recentComment : recentComments) {
                if (!recentComment.getUser().getId().equals(comment.getUser().getId())) {
                    // Simulate sending notification to commenter
                    logger.debug("Notifying user '{}' about new comment", 
                               recentComment.getUser().getUsername());
                    notificationCount++;
                }
            }

            Thread.sleep(300); // Simulate processing time

            logger.info("Notified {} other commenters about new comment", notificationCount);

        } catch (InterruptedException e) {
            logger.error("Other commenters notification interrupted for comment ID: {}", comment.getId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error notifying other commenters for comment ID: {}", comment.getId(), e);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Scheduled task to check for new comments every 5 minutes.
     * This demonstrates scheduled multithreading functionality.
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void checkForNewComments() {
        logger.debug("Scheduled task: Checking for new comments since: {}", lastCheckedTime);

        try {
            List<Comment> newComments = commentRepository.findCommentsAfterDate(lastCheckedTime);
            
            if (!newComments.isEmpty()) {
                logger.info("Found {} new comments since last check", newComments.size());
                
                // Process notifications for each new comment asynchronously
                for (Comment comment : newComments) {
                    processDelayedNotification(comment);
                }
            } else {
                logger.debug("No new comments found since last check");
            }

            lastCheckedTime = LocalDateTime.now();

        } catch (Exception e) {
            logger.error("Error in scheduled comment check", e);
        }
    }

    /**
     * Process delayed notification for comments that might have been missed.
     * 
     * @param comment the comment to process
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> processDelayedNotification(Comment comment) {
        try {
            logger.info("Processing delayed notification for comment ID: {}", comment.getId());

            // Check if notification was already sent (in real app, this would check a notification log)
            // For demo purposes, we'll just log the delayed notification

            Thread.sleep(200); // Simulate processing time

            logger.info("Delayed notification processed for comment ID: {}", comment.getId());

        } catch (InterruptedException e) {
            logger.error("Delayed notification processing interrupted for comment ID: {}", comment.getId(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error processing delayed notification for comment ID: {}", comment.getId(), e);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Get notification statistics.
     * 
     * @return notification statistics as a formatted string
     */
    public String getNotificationStatistics() {
        try {
            // Get recent comments for statistics
            List<Comment> recentComments = commentRepository
                    .findLatestComments(PageRequest.of(0, 10));

            StringBuilder stats = new StringBuilder();
            stats.append("Notification Statistics:\n");
            stats.append("- Last checked time: ").append(lastCheckedTime).append("\n");
            stats.append("- Recent comments count: ").append(recentComments.size()).append("\n");
            stats.append("- Notification service status: ACTIVE\n");

            return stats.toString();

        } catch (Exception e) {
            logger.error("Error getting notification statistics", e);
            return "Error retrieving notification statistics";
        }
    }

    /**
     * Bulk notification processing for multiple comments.
     * 
     * @param comments list of comments to process
     */
    @Async("taskExecutor")
    public CompletableFuture<Void> processBulkNotifications(List<Comment> comments) {
        try {
            logger.info("Processing bulk notifications for {} comments", comments.size());

            for (Comment comment : comments) {
                // Process each comment notification
                notifyNewComment(comment);
                
                // Add small delay to avoid overwhelming the system
                Thread.sleep(100);
            }

            logger.info("Bulk notification processing completed for {} comments", comments.size());

        } catch (InterruptedException e) {
            logger.error("Bulk notification processing interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Error in bulk notification processing", e);
        }

        return CompletableFuture.completedFuture(null);
    }
}