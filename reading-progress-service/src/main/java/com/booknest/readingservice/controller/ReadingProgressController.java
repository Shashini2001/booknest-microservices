package com.booknest.readingservice.controller;

import com.booknest.readingservice.model.ReadingProgress;
import com.booknest.readingservice.service.ReadingProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reading")
public class ReadingProgressController {

    @Autowired
    private ReadingProgressService service;

    @PostMapping
    public ResponseEntity<ReadingProgress> create(@RequestBody ReadingProgress entry) {
        return ResponseEntity.status(201).body(service.create(entry));
    }

    @GetMapping("/{userId}")
    public List<ReadingProgress> getForUser(@PathVariable String userId) {
        return service.getForUser(userId);
    }

    @GetMapping("/{userId}/favorites")
    public List<ReadingProgress> getFavorites(@PathVariable String userId) {
        return service.getFavorites(userId);
    }

    @GetMapping("/{userId}/stats")
    public Map<String, Object> getStats(@PathVariable String userId) {
        return service.getStats(userId);
    }

    @PutMapping("/entry/{id}")
    public ReadingProgress update(@PathVariable String id, @RequestBody ReadingProgress updates) {
        return service.update(id, updates);
    }

    @DeleteMapping("/entry/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
