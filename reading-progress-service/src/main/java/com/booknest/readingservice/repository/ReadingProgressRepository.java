package com.booknest.readingservice.repository;

import com.booknest.readingservice.model.ReadingProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingProgressRepository extends MongoRepository<ReadingProgress, String> {
    List<ReadingProgress> findByUserId(String userId);
    List<ReadingProgress> findByUserIdAndStatus(String userId, String status);
    List<ReadingProgress> findByUserIdAndIsFavoriteTrue(String userId);
    Optional<ReadingProgress> findByUserIdAndBookId(String userId, String bookId);
}
