package com.newsagency;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test for the News Agency Application.
 */
@SpringBootTest
@ActiveProfiles("test")
class NewsAgencyApplicationTests {

    /**
     * Test that the Spring context loads successfully.
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads without errors
    }

    /**
     * Test that the main application class can be instantiated.
     */
    @Test
    void mainApplicationClassExists() {
        // Verify that the main application class exists and can be instantiated
        NewsAgencyApplication app = new NewsAgencyApplication();
        assert app != null;
    }
}