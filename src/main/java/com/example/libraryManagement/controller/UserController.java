package com.example.libraryManagement.controller;

import com.example.libraryManagement.model.Book;
import com.example.libraryManagement.model.User;
import com.example.libraryManagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/read")
    public String read(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("showAddUser", true);
        model.addAttribute("showAddBook", false);
        return "user/read";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user/save";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/save";
        }
        userService.save(user);
        return "redirect:/user/read";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.delete(id);
        return "redirect:/user/read";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user/update";
        }
        return "redirect:/user/read";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/update";
        }
        user.setId(id);
        userService.save(user);
        return "redirect:/user/read";
    }
}
