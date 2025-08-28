package com.newsagency.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Database configuration class for JPA and transaction management.
 * 
 * This configuration enables JPA repositories and transaction management
 * for the news agency application.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.newsagency.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // JPA configuration is handled by Spring Boot auto-configuration
    // Custom database configurations can be added here if needed
}