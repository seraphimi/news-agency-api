# News Agency RESTful API

A comprehensive RESTful web service for news agency management built with Spring Boot 3.0 and JDK 17. This application provides complete functionality for managing users, authors, news articles, categories, and comments with advanced features including multithreading notifications, comprehensive logging, and production-ready architecture.

## 🚀 Features

### Core Functionality
- **User Management**: Registration, profile management, CRUD operations
- **Author Management**: Author information, contact details, specialization tracking
- **Category Management**: News categorization (sports, politics, culture, etc.)
- **News Article Management**: Complete article lifecycle with publishing workflow
- **Comment System**: User comments with real-time notifications

### Advanced Features
- **Multi-layer Architecture**: Controller → Service → Repository → Entity pattern
- **Multithreading**: Asynchronous notifications for comment alerts
- **Database Integration**: MySQL with JPA/Hibernate ORM
- **Comprehensive Logging**: Structured logging with file rotation
- **Exception Handling**: Global error handling with detailed responses
- **Input Validation**: Bean validation with custom error messages
- **Search Functionality**: Keyword search across news content
- **Pagination**: Built-in pagination for all list endpoints
- **View Counting**: Automatic view count tracking for news articles

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.0.12
- **Java Version**: JDK 17
- **Database**: MySQL 8.0+ (H2 for testing)
- **ORM**: JPA/Hibernate 6.0
- **Build Tool**: Maven 3.6+
- **Testing**: JUnit 5, Spring Boot Test
- **Logging**: SLF4J with Logback
- **Validation**: Bean Validation (JSR-303)
- **Documentation**: OpenAPI/Swagger (ready for integration)

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **JDK 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (or use H2 for testing)
- **Git** (for cloning the repository)

## 🔧 Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/seraphimi/news-agency-api.git
cd news-agency-api
```

### 2. Database Setup

#### MySQL Setup (Recommended for Production)
1. Install MySQL 8.0+ on your system
2. Create a database for the application:
```sql
CREATE DATABASE news_agency_db;
CREATE USER 'newsagency'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON news_agency_db.* TO 'newsagency'@'localhost';
FLUSH PRIVILEGES;
```

#### Configuration
Update the database configuration in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/news_agency_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: newsagency  # Update with your credentials
    password: password    # Update with your credentials
```

### 3. Build the Application
```bash
mvn clean compile
```

### 4. Run Tests
```bash
mvn test
```

### 5. Start the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication
Currently, the API operates without authentication for demonstration purposes. In a production environment, you should implement JWT or OAuth2 authentication.

## 🔗 API Endpoints

### Users Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/users` | Create a new user |
| GET    | `/api/users/{id}` | Get user by ID |
| GET    | `/api/users` | Get all users (paginated) |
| PUT    | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |
| GET    | `/api/users/search/firstname/{firstName}` | Search users by first name |
| GET    | `/api/users/search/lastname/{lastName}` | Search users by last name |

#### Example: Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepass123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Authors Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/authors` | Create a new author |
| GET    | `/api/authors/{id}` | Get author by ID |
| GET    | `/api/authors` | Get all authors (paginated) |
| PUT    | `/api/authors/{id}` | Update author |
| DELETE | `/api/authors/{id}` | Delete author |
| GET    | `/api/authors/search/name/{name}` | Search authors by name |
| GET    | `/api/authors/search/specialization/{specialization}` | Search by specialization |
| GET    | `/api/authors/published` | Get authors with published news |

#### Example: Create Author
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@news.com",
    "phoneNumber": "+1234567890",
    "bio": "Experienced journalist with 10 years in political reporting",
    "specialization": "Politics"
  }'
```

### Categories Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/categories` | Create a new category |
| GET    | `/api/categories/{id}` | Get category by ID |
| GET    | `/api/categories` | Get all categories (paginated) |
| PUT    | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |
| GET    | `/api/categories/search/name/{name}` | Search categories by name |
| GET    | `/api/categories/published` | Get categories with published news |
| GET    | `/api/categories/ordered-by-count` | Get categories ordered by news count |

#### Example: Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Technology",
    "description": "Latest technology news and innovations"
  }'
```

### News Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/news` | Create a new news article |
| GET    | `/api/news/{id}` | Get news by ID (increments view count) |
| GET    | `/api/news/published` | Get all published news (paginated) |
| GET    | `/api/news` | Get all news including drafts (paginated) |
| PUT    | `/api/news/{id}` | Update news article |
| DELETE | `/api/news/{id}` | Delete news article |
| GET    | `/api/news/search?keyword={keyword}` | Search news by keyword |
| GET    | `/api/news/author/{authorId}` | Get news by author |
| GET    | `/api/news/category/{categoryId}` | Get news by category |
| GET    | `/api/news/top` | Get top news by view count |
| GET    | `/api/news/recent?days={days}` | Get recent news |
| PUT    | `/api/news/{id}/publish` | Publish news article |
| PUT    | `/api/news/{id}/unpublish` | Unpublish news article |

#### Example: Create News Article
```bash
curl -X POST http://localhost:8080/api/news \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Breaking: New Technology Innovation",
    "content": "Detailed content of the news article...",
    "summary": "Brief summary of the article",
    "authorId": 1,
    "categoryId": 1,
    "published": false
  }'
```

#### Example: Search News
```bash
curl "http://localhost:8080/api/news/search?keyword=technology&page=0&size=10"
```

### Comments Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/comments` | Create a new comment |
| GET    | `/api/comments/{id}` | Get comment by ID |
| GET    | `/api/comments` | Get all comments (paginated) |
| PUT    | `/api/comments/{id}` | Update comment |
| DELETE | `/api/comments/{id}` | Delete comment |
| GET    | `/api/comments/news/{newsId}` | Get comments for a news article |
| GET    | `/api/comments/user/{userId}` | Get comments by user |
| GET    | `/api/comments/search?keyword={keyword}` | Search comments by content |
| GET    | `/api/comments/news/{newsId}/recent?hours={hours}` | Get recent comments for news |
| GET    | `/api/comments/news/{newsId}/count` | Count comments for news |
| GET    | `/api/comments/user/{userId}/count` | Count comments by user |

#### Example: Create Comment
```bash
curl -X POST http://localhost:8080/api/comments \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Great article! Very informative.",
    "userId": 1,
    "newsId": 1
  }'
```

## 🔍 Search and Filtering

### News Search
The API supports powerful search functionality:
```bash
# Search in title, content, and summary
GET /api/news/search?keyword=technology

# Get recent news from last 7 days
GET /api/news/recent?days=7

# Get top viewed articles
GET /api/news/top?page=0&size=5
```

### Pagination
All list endpoints support pagination:
```bash
# Get second page with 20 items per page
GET /api/users?page=1&size=20

# Sort by creation date descending
GET /api/news?page=0&size=10&sort=createdAt,desc
```

## 🔔 Multithreading and Notifications

The application implements sophisticated multithreading for notifications:

### Features
- **Asynchronous Processing**: Comments trigger immediate notifications
- **Author Notifications**: Authors are notified when their articles receive comments
- **Commenter Notifications**: Users are notified about new comments on articles they've commented on
- **Scheduled Tasks**: Background tasks check for new comments every 5 minutes
- **Thread Pool Management**: Configurable thread pool for optimal performance

### Configuration
Thread pool settings in `src/main/java/com/newsagency/config/AsyncConfig.java`:
```java
executor.setCorePoolSize(4);
executor.setMaxPoolSize(8);
executor.setQueueCapacity(100);
```

## 📊 Logging

### Log Levels
- **DEBUG**: Detailed information for debugging
- **INFO**: General application flow information
- **WARN**: Warning messages for potential issues
- **ERROR**: Error events that might allow the application to continue

### Log Files
- **Main Log**: `logs/news-agency-api.log`
- **Error Log**: `logs/news-agency-api-error.log`

### Log Configuration
Logging is configured in `src/main/resources/logback-spring.xml` with:
- Console and file appenders
- Daily log rotation
- Maximum file sizes (10MB)
- Retention policy (30 days)
- Asynchronous logging for performance

## ⚠️ Error Handling

The API provides comprehensive error handling with detailed responses:

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: 123",
  "path": "/api/users/123"
}
```

### Validation Errors
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "validationErrors": {
    "email": "Email should be valid",
    "username": "Username must be between 3 and 50 characters"
  },
  "path": "/api/users"
}
```

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Configuration
Tests use H2 in-memory database with the configuration in `src/test/resources/application-test.yml`.

### Testing with Postman

1. **Import the API endpoints** into Postman
2. **Set base URL**: `http://localhost:8080/api`
3. **Create test data** in this order:
   - Authors (for news articles)
   - Categories (for news articles)
   - Users (for comments)
   - News articles
   - Comments

### Sample Test Flow
```bash
# 1. Create an author
POST /api/authors
{
  "name": "Test Author",
  "email": "author@test.com",
  "specialization": "Technology"
}

# 2. Create a category
POST /api/categories
{
  "name": "Tech News",
  "description": "Technology related news"
}

# 3. Create a user
POST /api/users
{
  "username": "testuser",
  "email": "user@test.com",
  "password": "password123",
  "firstName": "Test",
  "lastName": "User"
}

# 4. Create a news article
POST /api/news
{
  "title": "Test Article",
  "content": "This is a test article content...",
  "summary": "Test summary",
  "authorId": 1,
  "categoryId": 1
}

# 5. Publish the article
PUT /api/news/1/publish

# 6. Add a comment
POST /api/comments
{
  "content": "Great article!",
  "userId": 1,
  "newsId": 1
}
```

## 🏗 Project Structure

```
src/
├── main/
│   ├── java/com/newsagency/
│   │   ├── NewsAgencyApplication.java          # Main Spring Boot application
│   │   ├── config/
│   │   │   ├── DatabaseConfig.java             # JPA configuration
│   │   │   └── AsyncConfig.java                # Async/multithreading config
│   │   ├── controller/                         # REST Controllers
│   │   │   ├── UserController.java
│   │   │   ├── AuthorController.java
│   │   │   ├── CategoryController.java
│   │   │   ├── NewsController.java
│   │   │   └── CommentController.java
│   │   ├── service/                            # Business Logic Layer
│   │   │   ├── UserService.java
│   │   │   ├── AuthorService.java
│   │   │   ├── CategoryService.java
│   │   │   ├── NewsService.java
│   │   │   ├── CommentService.java
│   │   │   └── NotificationService.java        # Multithreading notifications
│   │   ├── repository/                         # Data Access Layer
│   │   │   ├── UserRepository.java
│   │   │   ├── AuthorRepository.java
│   │   │   ├── CategoryRepository.java
│   │   │   ├── NewsRepository.java
│   │   │   └── CommentRepository.java
│   │   ├── entity/                             # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Author.java
│   │   │   ├── Category.java
│   │   │   ├── News.java
│   │   │   └── Comment.java
│   │   ├── dto/                                # Data Transfer Objects
│   │   │   ├── UserDTO.java
│   │   │   ├── AuthorDTO.java
│   │   │   ├── CategoryDTO.java
│   │   │   ├── NewsDTO.java
│   │   │   └── CommentDTO.java
│   │   └── exception/                          # Exception Handling
│   │       ├── GlobalExceptionHandler.java
│   │       └── ResourceNotFoundException.java
│   └── resources/
│       ├── application.yml                     # Application configuration
│       └── logback-spring.xml                  # Logging configuration
└── test/
    ├── java/com/newsagency/
    │   └── NewsAgencyApplicationTests.java     # Integration tests
    └── resources/
        └── application-test.yml                # Test configuration
```

## 🚀 Production Deployment

### Environment Variables
For production deployment, use environment variables for sensitive configuration:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-server:3306/news_agency_db
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export SPRING_PROFILES_ACTIVE=production
```

### Database Migration
For production, consider using Flyway or Liquibase for database migrations instead of `hibernate.ddl-auto=update`.

### Security Considerations
- Implement authentication (JWT/OAuth2)
- Enable HTTPS
- Configure CORS for specific domains
- Implement rate limiting
- Add input sanitization
- Enable database encryption
- Set up monitoring and alerting

### Performance Optimization
- Configure connection pooling
- Implement caching (Redis/Hazelcast)
- Enable database indexing
- Configure JVM memory settings
- Set up load balancing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Change port in application.yml
   server:
     port: 8081
   ```

2. **MySQL connection refused**
   - Ensure MySQL is running
   - Check connection credentials
   - Verify database exists

3. **Tests failing**
   ```bash
   # Clean and rebuild
   mvn clean compile test
   ```

4. **Log files not created**
   - Ensure `logs/` directory exists
   - Check file permissions

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the logs in `logs/news-agency-api.log` for error details
- Review the API documentation and examples above

---

**Note**: This is a demonstration project for educational purposes. For production use, implement proper security measures, authentication, and follow best practices for your specific deployment environment.