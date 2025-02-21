package com.example.libraryManagement.service;

import com.example.libraryManagement.model.Book;
import com.example.libraryManagement.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;

    public List<Book> getAllBooks() {
        return libraryRepository.findAll();
    }

    public void save(Book book) {
        libraryRepository.save(book);
    }

    public Optional<Book> findById(int id) {
        return libraryRepository.findById(id);
    }

    public void delete(int id) {
        libraryRepository.deleteById(id);
    }

    public Book updateBook(Book product) {
        return libraryRepository.save(product);
    }
}
