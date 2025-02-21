package com.example.libraryManagement.repository;

import com.example.libraryManagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Book, Integer> {

}

