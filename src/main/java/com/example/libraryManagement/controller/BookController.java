package com.example.libraryManagement.controller;

import com.example.libraryManagement.model.Book;
import com.example.libraryManagement.service.JwtService;
import com.example.libraryManagement.service.LibraryService;
import jakarta.servlet.http.HttpSession;
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
    private final JwtService jwtService;

    public BookController(LibraryService libraryService, JwtService jwtService) {
        this.libraryService = libraryService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (token != null && jwtService.validateToken(token)) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "index";
    }

    @GetMapping("/read")
    public String read(Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (token != null && jwtService.validateToken(token)) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("loggedIn", false);
        }
        model.addAttribute("books", libraryService.getAllBooks());
        model.addAttribute("showAddUser", false);
        model.addAttribute("showAddBook", true);
        model.addAttribute("username", session.getAttribute("username"));
        return "book/read";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (token != null && jwtService.validateToken(token)) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("loggedIn", false);
        }
        model.addAttribute("book", new Book());
        model.addAttribute("username", session.getAttribute("username"));
        return "book/save";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute Book book, BindingResult bindingResult, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("username", session.getAttribute("username"));
            return "book/save";
        }
        libraryService.save(book);
        return "redirect:/read";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        libraryService.delete(id);
        return "redirect:/read";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (token != null && jwtService.validateToken(token)) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("username", session.getAttribute("username"));
        } else {
            model.addAttribute("loggedIn", false);
        }
        Optional<Book> bookOptional = libraryService.findById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            model.addAttribute("username", session.getAttribute("username"));
            return "book/update";
        }
        return "redirect:/read";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable int id, @Valid @ModelAttribute Book book, BindingResult bindingResult, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("username", session.getAttribute("username"));
            return "book/update";
        }
        book.setId(id);
        libraryService.updateBook(book);
        return "redirect:/read";
    }
}