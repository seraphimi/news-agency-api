package com.newsagency.controller;

import com.newsagency.entity.Author;
import com.newsagency.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.findById(id);
        return author.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.save(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Optional<Author> optionalAuthor = authorService.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(authorDetails.getName());
            author.setEmail(authorDetails.getEmail());
            author.setBio(authorDetails.getBio());
            return ResponseEntity.ok(authorService.save(author));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        if (authorService.findById(id).isPresent()) {
            authorService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}