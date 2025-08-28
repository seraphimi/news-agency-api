package com.newsagency.service;

import com.newsagency.entity.Comment;
import com.newsagency.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}