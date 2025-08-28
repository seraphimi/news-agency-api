package com.newsagency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the News Agency API.
 * 
 * This application provides a comprehensive RESTful web service for news agency management
 * with features including user management, author management, news articles, categories,
 * and comment management with multithreading support for notifications.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class NewsAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsAgencyApplication.class, args);
    }
}