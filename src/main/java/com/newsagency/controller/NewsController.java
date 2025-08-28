package com.newsagency.controller;

import com.newsagency.dto.NewsDTO;
import com.newsagency.service.NewsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for News management operations.
 */
@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    /**
     * Create a new news article.
     *
     * @param newsDTO the news data
     * @return ResponseEntity with created news
     */
    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO) {
        logger.info("REST request to create news: {}", newsDTO.getTitle());
        NewsDTO createdNews = newsService.createNews(newsDTO);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    /**
     * Get news by ID.
     *
     * @param id the news ID
     * @return ResponseEntity with news data
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        logger.debug("REST request to get news by ID: {}", id);
        NewsDTO news = newsService.getNewsAndIncrementViewCount(id);
        return ResponseEntity.ok(news);
    }

    /**
     * Get all published news with pagination.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of published news
     */
    @GetMapping("/published")
    public ResponseEntity<Page<NewsDTO>> getAllPublishedNews(Pageable pageable) {
        logger.debug("REST request to get all published news");
        Page<NewsDTO> news = newsService.getAllPublishedNews(pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Get all news with pagination (including unpublished).
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of all news
     */
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(Pageable pageable) {
        logger.debug("REST request to get all news");
        Page<NewsDTO> news = newsService.getAllNews(pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Update news article.
     *
     * @param id the news ID
     * @param newsDTO the updated news data
     * @return ResponseEntity with updated news
     */
    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, 
                                             @Valid @RequestBody NewsDTO newsDTO) {
        logger.info("REST request to update news with ID: {}", id);
        NewsDTO updatedNews = newsService.updateNews(id, newsDTO);
        return ResponseEntity.ok(updatedNews);
    }

    /**
     * Delete news by ID.
     *
     * @param id the news ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        logger.info("REST request to delete news with ID: {}", id);
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search news by keyword.
     *
     * @param keyword the keyword to search for
     * @param pageable pagination parameters
     * @return ResponseEntity with page of news matching the keyword
     */
    @GetMapping("/search")
    public ResponseEntity<Page<NewsDTO>> searchByKeyword(@RequestParam String keyword, 
                                                        Pageable pageable) {
        logger.debug("REST request to search news by keyword: {}", keyword);
        Page<NewsDTO> news = newsService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Get news by author.
     *
     * @param authorId the author ID
     * @param pageable pagination parameters
     * @return ResponseEntity with page of news by the author
     */
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<NewsDTO>> getNewsByAuthor(@PathVariable Long authorId, 
                                                        Pageable pageable) {
        logger.debug("REST request to get news by author ID: {}", authorId);
        Page<NewsDTO> news = newsService.getNewsByAuthor(authorId, pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Get news by category.
     *
     * @param categoryId the category ID
     * @param pageable pagination parameters
     * @return ResponseEntity with page of news in the category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<NewsDTO>> getNewsByCategory(@PathVariable Long categoryId, 
                                                          Pageable pageable) {
        logger.debug("REST request to get news by category ID: {}", categoryId);
        Page<NewsDTO> news = newsService.getNewsByCategory(categoryId, pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Get top news by view count.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of top news
     */
    @GetMapping("/top")
    public ResponseEntity<Page<NewsDTO>> getTopNewsByViewCount(Pageable pageable) {
        logger.debug("REST request to get top news by view count");
        Page<NewsDTO> news = newsService.getTopNewsByViewCount(pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Get recent news.
     *
     * @param days number of days to look back (default: 7)
     * @param pageable pagination parameters
     * @return ResponseEntity with page of recent news
     */
    @GetMapping("/recent")
    public ResponseEntity<Page<NewsDTO>> getRecentNews(@RequestParam(defaultValue = "7") int days, 
                                                      Pageable pageable) {
        logger.debug("REST request to get recent news from last {} days", days);
        Page<NewsDTO> news = newsService.getRecentNews(days, pageable);
        return ResponseEntity.ok(news);
    }

    /**
     * Publish news article.
     *
     * @param id the news ID
     * @return ResponseEntity with published news
     */
    @PutMapping("/{id}/publish")
    public ResponseEntity<NewsDTO> publishNews(@PathVariable Long id) {
        logger.info("REST request to publish news with ID: {}", id);
        NewsDTO publishedNews = newsService.publishNews(id);
        return ResponseEntity.ok(publishedNews);
    }

    /**
     * Unpublish news article.
     *
     * @param id the news ID
     * @return ResponseEntity with unpublished news
     */
    @PutMapping("/{id}/unpublish")
    public ResponseEntity<NewsDTO> unpublishNews(@PathVariable Long id) {
        logger.info("REST request to unpublish news with ID: {}", id);
        NewsDTO unpublishedNews = newsService.unpublishNews(id);
        return ResponseEntity.ok(unpublishedNews);
    }
}