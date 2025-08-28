package com.newsagency.controller;

import com.newsagency.dto.CategoryDTO;
import com.newsagency.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category management operations.
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    /**
     * Create a new category.
     *
     * @param categoryDTO the category data
     * @return ResponseEntity with created category
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("REST request to create category: {}", categoryDTO.getName());
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * Get category by ID.
     *
     * @param id the category ID
     * @return ResponseEntity with category data
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.debug("REST request to get category by ID: {}", id);
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Get all categories with pagination.
     *
     * @param pageable pagination parameters
     * @return ResponseEntity with page of categories
     */
    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(Pageable pageable) {
        logger.debug("REST request to get all categories");
        Page<CategoryDTO> categories = categoryService.getAllCategories(pageable);
        return ResponseEntity.ok(categories);
    }

    /**
     * Update category.
     *
     * @param id the category ID
     * @param categoryDTO the updated category data
     * @return ResponseEntity with updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, 
                                                     @Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("REST request to update category with ID: {}", id);
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Delete category by ID.
     *
     * @param id the category ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        logger.info("REST request to delete category with ID: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search categories by name.
     *
     * @param name the name to search for
     * @return ResponseEntity with list of categories
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<CategoryDTO>> searchByName(@PathVariable String name) {
        logger.debug("REST request to search categories by name: {}", name);
        List<CategoryDTO> categories = categoryService.searchByName(name);
        return ResponseEntity.ok(categories);
    }

    /**
     * Get categories with published news.
     *
     * @return ResponseEntity with list of categories with published news
     */
    @GetMapping("/published")
    public ResponseEntity<List<CategoryDTO>> getCategoriesWithPublishedNews() {
        logger.debug("REST request to get categories with published news");
        List<CategoryDTO> categories = categoryService.getCategoriesWithPublishedNews();
        return ResponseEntity.ok(categories);
    }

    /**
     * Get categories ordered by news count.
     *
     * @return ResponseEntity with list of categories ordered by news count
     */
    @GetMapping("/ordered-by-count")
    public ResponseEntity<List<CategoryDTO>> getCategoriesOrderByNewsCount() {
        logger.debug("REST request to get categories ordered by news count");
        List<CategoryDTO> categories = categoryService.getCategoriesOrderByNewsCount();
        return ResponseEntity.ok(categories);
    }
}