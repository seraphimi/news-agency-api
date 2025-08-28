# News Agency RESTful API

A simple RESTful web service for news agency management built with Spring Boot 3.0 and JDK 17. This is a school project demonstrating basic Spring Boot concepts with essential CRUD operations.

## Features

- User management
- Author management  
- News category management
- News article management
- Comment management
- Simple REST API endpoints
- MySQL database integration
- JPA/Hibernate for data persistence

## Technology Stack

- **Spring Boot 3.0** - Main framework
- **JDK 17** - Java version
- **Spring Data JPA** - Data persistence
- **MySQL** - Database
- **Maven** - Build tool

## Project Structure

```
src/main/java/com/newsagency/
├── NewsAgencyApplication.java          # Main Spring Boot application
├── controller/                         # REST Controllers
│   ├── UserController.java
│   ├── AuthorController.java
│   ├── CategoryController.java
│   ├── NewsController.java
│   └── CommentController.java
├── service/                           # Business Logic Layer
│   ├── UserService.java
│   ├── AuthorService.java
│   ├── CategoryService.java
│   ├── NewsService.java
│   └── CommentService.java
├── repository/                        # Data Access Layer
│   ├── UserRepository.java
│   ├── AuthorRepository.java
│   ├── CategoryRepository.java
│   ├── NewsRepository.java
│   └── CommentRepository.java
└── entity/                           # JPA Entities
    ├── User.java
    ├── Author.java
    ├── Category.java
    ├── News.java
    └── Comment.java
```

## Setup Instructions

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE news_agency_db;
```

2. Update the database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/news_agency_db
spring.datasource.username=root
spring.datasource.password=password
```

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd news-agency-api
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Authors
- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get author by ID
- `POST /api/authors` - Create new author
- `PUT /api/authors/{id}` - Update author
- `DELETE /api/authors/{id}` - Delete author

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

### News
- `GET /api/news` - Get all news articles
- `GET /api/news/{id}` - Get news article by ID
- `POST /api/news` - Create new news article
- `PUT /api/news/{id}` - Update news article
- `DELETE /api/news/{id}` - Delete news article

### Comments
- `GET /api/comments` - Get all comments
- `GET /api/comments/{id}` - Get comment by ID
- `POST /api/comments` - Create new comment
- `PUT /api/comments/{id}` - Update comment
- `DELETE /api/comments/{id}` - Delete comment

## Testing with Postman

### Create a User
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
}
```

### Create an Author
```
POST http://localhost:8080/api/authors
Content-Type: application/json

{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "bio": "Experienced journalist with 10 years in the field"
}
```

### Create a Category
```
POST http://localhost:8080/api/categories
Content-Type: application/json

{
    "name": "Technology",
    "description": "Latest technology news and updates"
}
```

### Create News Article
```
POST http://localhost:8080/api/news
Content-Type: application/json

{
    "title": "Latest Tech News",
    "content": "This is the content of the news article...",
    "author": {
        "id": 1
    },
    "category": {
        "id": 1
    }
}
```

### Create a Comment
```
POST http://localhost:8080/api/comments
Content-Type: application/json

{
    "content": "Great article! Very informative.",
    "user": {
        "id": 1
    },
    "news": {
        "id": 1
    }
}
```

## Entity Relationships

- **Author** → **News** (One-to-Many): One author can write multiple news articles
- **Category** → **News** (One-to-Many): One category can contain multiple news articles  
- **News** → **Comments** (One-to-Many): One news article can have multiple comments
- **User** → **Comments** (One-to-Many): One user can write multiple comments

## Development Notes

This is a simple school project focusing on:
- Basic Spring Boot application structure
- RESTful API design
- JPA entity relationships
- CRUD operations
- Clean code organization

The project intentionally keeps things simple without complex features like validation, security, DTOs, or advanced error handling to focus on core Spring Boot concepts.
