package com.example.libraryManagement.controller;

import com.example.libraryManagement.model.Book;
import com.example.libraryManagement.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class BookController {
    private final LibraryService libraryService;

    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/read")
    public String read(Model model) {
        model.addAttribute("books", libraryService.getAllBooks());
        model.addAttribute("showAddUser", false);
        model.addAttribute("showAddBook", true);
        return "book/read";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        return "book/save";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            return "book/save";
        }
        libraryService.save(book);
        return "redirect:/read";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        libraryService.delete(id);
        return "redirect:/read";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<Book> bookOptional = libraryService.findById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            return "book/update";
        }
        return "redirect:/read";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute Book book) {
        libraryService.updateBook(book);
        return "redirect:/read";
    }
}