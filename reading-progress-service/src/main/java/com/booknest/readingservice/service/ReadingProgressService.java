package com.booknest.readingservice.service;

import com.booknest.readingservice.model.ReadingProgress;
import com.booknest.readingservice.repository.ReadingProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReadingProgressService {

    @Autowired
    private ReadingProgressRepository repository;

    public ReadingProgress create(ReadingProgress entry) {
        if (entry.getStatus() == null) {
            entry.setStatus("WISHLIST");
        }
        if ("READING".equals(entry.getStatus()) && entry.getStartedAt() == null) {
            entry.setStartedAt(LocalDateTime.now());
        }
        return repository.save(entry);
    }

    public List<ReadingProgress> getForUser(String userId) {
        return repository.findByUserId(userId);
    }

    public List<ReadingProgress> getFavorites(String userId) {
        return repository.findByUserIdAndIsFavoriteTrue(userId);
    }

    public ReadingProgress getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading record not found: " + id));
    }

    public ReadingProgress update(String id, ReadingProgress updates) {
        ReadingProgress existing = getById(id);

        if (updates.getStatus() != null) {
            existing.setStatus(updates.getStatus());
            if ("COMPLETED".equals(updates.getStatus()) && existing.getCompletedAt() == null) {
                existing.setCompletedAt(LocalDateTime.now());
            }
            if ("READING".equals(updates.getStatus()) && existing.getStartedAt() == null) {
                existing.setStartedAt(LocalDateTime.now());
            }
        }
        if (updates.getPagesRead() != 0) existing.setPagesRead(updates.getPagesRead());
        if (updates.getTotalPages() != 0) existing.setTotalPages(updates.getTotalPages());
        existing.setFavorite(updates.isFavorite());
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    // Powers the Reading Dashboard chart on the frontend
    public Map<String, Object> getStats(String userId) {
        List<ReadingProgress> completed = repository.findByUserIdAndStatus(userId, "COMPLETED");
        List<ReadingProgress> reading = repository.findByUserIdAndStatus(userId, "READING");

        Map<String, Long> byMonth = completed.stream()
                .filter(r -> r.getCompletedAt() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getCompletedAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("totalBooksRead", completed.size());
        result.put("currentlyReading", reading.size());
        result.put("byMonth", byMonth);
        return result;
    }
}
