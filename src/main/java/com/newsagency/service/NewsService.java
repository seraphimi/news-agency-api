package com.newsagency.service;

import com.newsagency.dto.NewsDTO;
import com.newsagency.entity.Author;
import com.newsagency.entity.Category;
import com.newsagency.entity.News;
import com.newsagency.exception.ResourceNotFoundException;
import com.newsagency.repository.AuthorRepository;
import com.newsagency.repository.CategoryRepository;
import com.newsagency.repository.NewsRepository;
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
 * Service class for News entity operations.
 */
@Service
@Transactional
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsRepository newsRepository;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new news article.
     *
     * @param newsDTO the news data
     * @return created news DTO
     */
    public NewsDTO createNews(NewsDTO newsDTO) {
        logger.info("Creating new news article with title: {}", newsDTO.getTitle());
        
        Author author = authorRepository.findById(newsDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + newsDTO.getAuthorId()));
        
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + newsDTO.getCategoryId()));

        News news = convertToEntity(newsDTO, author, category);
        News savedNews = newsRepository.save(news);
        
        logger.info("News article created successfully with ID: {}", savedNews.getId());
        return convertToDTO(savedNews);
    }

    /**
     * Get news by ID.
     *
     * @param id the news ID
     * @return news DTO
     */
    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        logger.debug("Fetching news with ID: {}", id);
        
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + id));
        
        return convertToDTO(news);
    }

    /**
     * Get news by ID and increment view count.
     *
     * @param id the news ID
     * @return news DTO
     */
    public NewsDTO getNewsAndIncrementViewCount(Long id) {
        logger.debug("Fetching news with ID: {} and incrementing view count", id);
        
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + id));
        
        // Increment view count
        news.incrementViewCount();
        newsRepository.save(news);
        
        return convertToDTO(news);
    }

    /**
     * Get all published news with pagination.
     *
     * @param pageable pagination information
     * @return page of news DTOs
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getAllPublishedNews(Pageable pageable) {
        logger.debug("Fetching all published news with pagination");
        
        Page<News> news = newsRepository.findByPublishedTrueOrderByPublishedAtDesc(pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Get all news with pagination (including unpublished).
     *
     * @param pageable pagination information
     * @return page of news DTOs
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getAllNews(Pageable pageable) {
        logger.debug("Fetching all news with pagination");
        
        Page<News> news = newsRepository.findAll(pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Update news article.
     *
     * @param id the news ID
     * @param newsDTO the updated news data
     * @return updated news DTO
     */
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        logger.info("Updating news with ID: {}", id);
        
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + id));

        Author author = authorRepository.findById(newsDTO.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + newsDTO.getAuthorId()));
        
        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + newsDTO.getCategoryId()));

        existingNews.setTitle(newsDTO.getTitle());
        existingNews.setContent(newsDTO.getContent());
        existingNews.setSummary(newsDTO.getSummary());
        existingNews.setAuthor(author);
        existingNews.setCategory(category);
        
        // Handle publishing
        if (newsDTO.getPublished() != null && newsDTO.getPublished() != existingNews.getPublished()) {
            existingNews.setPublished(newsDTO.getPublished());
        }

        News updatedNews = newsRepository.save(existingNews);
        
        logger.info("News updated successfully with ID: {}", updatedNews.getId());
        return convertToDTO(updatedNews);
    }

    /**
     * Delete news by ID.
     *
     * @param id the news ID
     */
    public void deleteNews(Long id) {
        logger.info("Deleting news with ID: {}", id);
        
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException("News not found with ID: " + id);
        }

        newsRepository.deleteById(id);
        logger.info("News deleted successfully with ID: {}", id);
    }

    /**
     * Search news by keyword.
     *
     * @param keyword the keyword to search for
     * @param pageable pagination information
     * @return page of news DTOs matching the keyword
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> searchByKeyword(String keyword, Pageable pageable) {
        logger.debug("Searching news by keyword: {}", keyword);
        
        Page<News> news = newsRepository.searchByKeyword(keyword, pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Get news by author.
     *
     * @param authorId the author ID
     * @param pageable pagination information
     * @return page of news DTOs by the author
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getNewsByAuthor(Long authorId, Pageable pageable) {
        logger.debug("Fetching news by author ID: {}", authorId);
        
        Page<News> news = newsRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Get news by category.
     *
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of news DTOs in the category
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getNewsByCategory(Long categoryId, Pageable pageable) {
        logger.debug("Fetching news by category ID: {}", categoryId);
        
        Page<News> news = newsRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Get top news by view count.
     *
     * @param pageable pagination information
     * @return page of top news DTOs
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getTopNewsByViewCount(Pageable pageable) {
        logger.debug("Fetching top news by view count");
        
        Page<News> news = newsRepository.findTopNewsByViewCount(pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Get recent news.
     *
     * @param days number of days to look back
     * @param pageable pagination information
     * @return page of recent news DTOs
     */
    @Transactional(readOnly = true)
    public Page<NewsDTO> getRecentNews(int days, Pageable pageable) {
        logger.debug("Fetching recent news from last {} days", days);
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        Page<News> news = newsRepository.findRecentNews(since, pageable);
        return news.map(this::convertToDTO);
    }

    /**
     * Publish news article.
     *
     * @param id the news ID
     * @return updated news DTO
     */
    public NewsDTO publishNews(Long id) {
        logger.info("Publishing news with ID: {}", id);
        
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + id));
        
        news.setPublished(true);
        News publishedNews = newsRepository.save(news);
        
        logger.info("News published successfully with ID: {}", id);
        return convertToDTO(publishedNews);
    }

    /**
     * Unpublish news article.
     *
     * @param id the news ID
     * @return updated news DTO
     */
    public NewsDTO unpublishNews(Long id) {
        logger.info("Unpublishing news with ID: {}", id);
        
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with ID: " + id));
        
        news.setPublished(false);
        News unpublishedNews = newsRepository.save(news);
        
        logger.info("News unpublished successfully with ID: {}", id);
        return convertToDTO(unpublishedNews);
    }

    /**
     * Convert News entity to NewsDTO.
     *
     * @param news the news entity
     * @return news DTO
     */
    private NewsDTO convertToDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(news.getId());
        newsDTO.setTitle(news.getTitle());
        newsDTO.setContent(news.getContent());
        newsDTO.setSummary(news.getSummary());
        newsDTO.setPublished(news.getPublished());
        newsDTO.setViewCount(news.getViewCount());
        newsDTO.setAuthorId(news.getAuthor().getId());
        newsDTO.setAuthorName(news.getAuthor().getName());
        newsDTO.setCategoryId(news.getCategory().getId());
        newsDTO.setCategoryName(news.getCategory().getName());
        newsDTO.setCreatedAt(news.getCreatedAt());
        newsDTO.setUpdatedAt(news.getUpdatedAt());
        newsDTO.setPublishedAt(news.getPublishedAt());
        return newsDTO;
    }

    /**
     * Convert NewsDTO to News entity.
     *
     * @param newsDTO the news DTO
     * @param author the author entity
     * @param category the category entity
     * @return news entity
     */
    private News convertToEntity(NewsDTO newsDTO, Author author, Category category) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setSummary(newsDTO.getSummary());
        news.setPublished(newsDTO.getPublished() != null ? newsDTO.getPublished() : false);
        news.setAuthor(author);
        news.setCategory(category);
        return news;
    }
}