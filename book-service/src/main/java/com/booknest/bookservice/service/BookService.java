package com.booknest.bookservice.service;

import com.booknest.bookservice.model.Book;
import com.booknest.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public Book create(Book book) {
        return repository.save(book);
    }

    public List<Book> getAll() {
        return repository.findAll();
    }

    public Book getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public Book update(String id, Book updated) {
        Book existing = getById(id);
        updated.setId(existing.getId());
        return repository.save(updated);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Book> getByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    public List<Book> search(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }
}
