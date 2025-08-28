package com.newsagency.service;

import com.newsagency.dto.CategoryDTO;
import com.newsagency.entity.Category;
import com.newsagency.exception.ResourceNotFoundException;
import com.newsagency.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Category entity operations.
 */
@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new category.
     *
     * @param categoryDTO the category data
     * @return created category DTO
     */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        logger.info("Creating new category with name: {}", categoryDTO.getName());
        
        // Check if category name already exists
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDTO.getName());
        }

        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        
        logger.info("Category created successfully with ID: {}", savedCategory.getId());
        return convertToDTO(savedCategory);
    }

    /**
     * Get category by ID.
     *
     * @param id the category ID
     * @return category DTO
     */
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        logger.debug("Fetching category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        return convertToDTO(category);
    }

    /**
     * Get all categories with pagination.
     *
     * @param pageable pagination information
     * @return page of category DTOs
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        logger.debug("Fetching all categories with pagination");
        
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::convertToDTO);
    }

    /**
     * Update category.
     *
     * @param id the category ID
     * @param categoryDTO the updated category data
     * @return updated category DTO
     */
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        logger.info("Updating category with ID: {}", id);
        
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Check if new name conflicts with another category
        if (!existingCategory.getName().equals(categoryDTO.getName()) && 
            categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + categoryDTO.getName());
        }

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);
        
        logger.info("Category updated successfully with ID: {}", updatedCategory.getId());
        return convertToDTO(updatedCategory);
    }

    /**
     * Delete category by ID.
     *
     * @param id the category ID
     */
    public void deleteCategory(Long id) {
        logger.info("Deleting category with ID: {}", id);
        
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        categoryRepository.deleteById(id);
        logger.info("Category deleted successfully with ID: {}", id);
    }

    /**
     * Search categories by name.
     *
     * @param name the name to search for
     * @return list of category DTOs
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> searchByName(String name) {
        logger.debug("Searching categories by name: {}", name);
        
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get categories with published news.
     *
     * @return list of category DTOs with published news
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesWithPublishedNews() {
        logger.debug("Fetching categories with published news");
        
        List<Category> categories = categoryRepository.findCategoriesWithPublishedNews();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get categories ordered by news count.
     *
     * @return list of category DTOs ordered by news count
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoriesOrderByNewsCount() {
        logger.debug("Fetching categories ordered by news count");
        
        List<Category> categories = categoryRepository.findCategoriesOrderByNewsCount();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Category entity to CategoryDTO.
     *
     * @param category the category entity
     * @return category DTO
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }

    /**
     * Convert CategoryDTO to Category entity.
     *
     * @param categoryDTO the category DTO
     * @return category entity
     */
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }
}