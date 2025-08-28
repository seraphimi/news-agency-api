package com.newsagency.controller;

import com.newsagency.entity.News;
import com.newsagency.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;

    @GetMapping
    public List<News> getAllNews() {
        return newsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.findById(id);
        return news.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public News createNews(@RequestBody News news) {
        return newsService.save(news);
    }

    @PutMapping("/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News newsDetails) {
        Optional<News> optionalNews = newsService.findById(id);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            news.setTitle(newsDetails.getTitle());
            news.setContent(newsDetails.getContent());
            news.setAuthor(newsDetails.getAuthor());
            news.setCategory(newsDetails.getCategory());
            return ResponseEntity.ok(newsService.save(news));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        if (newsService.findById(id).isPresent()) {
            newsService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}