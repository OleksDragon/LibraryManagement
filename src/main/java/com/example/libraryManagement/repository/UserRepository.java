package com.example.libraryManagement.repository;

import com.example.libraryManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Пошук користувача за ім'ям
    Optional<User> findByName(String name);
}
