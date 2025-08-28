package com.newsagency.controller;

import com.newsagency.entity.Comment;
import com.newsagency.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        return comment.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {
        Optional<Comment> optionalComment = commentService.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(commentDetails.getContent());
            comment.setUser(commentDetails.getUser());
            comment.setNews(commentDetails.getNews());
            return ResponseEntity.ok(commentService.save(comment));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        if (commentService.findById(id).isPresent()) {
            commentService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}