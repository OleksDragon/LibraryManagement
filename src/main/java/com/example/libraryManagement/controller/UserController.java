package com.example.libraryManagement.controller;

import com.example.libraryManagement.model.User;
import com.example.libraryManagement.service.JwtService;
import com.example.libraryManagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("showAddUser", true);
        model.addAttribute("showAddBook", false);
        model.addAttribute("username", session.getAttribute("username"));
        return "user/read";
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
        model.addAttribute("user", new User());
        model.addAttribute("username", session.getAttribute("username"));
        return "user/save";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("username", session.getAttribute("username"));
            return "user/save";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/user/read";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        userService.delete(id);
        return "redirect:/user/read";
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
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("username", session.getAttribute("username"));
            return "user/update";
        }
        return "redirect:/user/read";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute User user, BindingResult bindingResult, Model model, HttpSession session) {
        String token = (String) session.getAttribute("jwtToken");
        if (token == null || !jwtService.validateToken(token)) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("username", session.getAttribute("username"));
            return "user/update";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(id);
        userService.save(user);
        return "redirect:/user/read";
    }
}
