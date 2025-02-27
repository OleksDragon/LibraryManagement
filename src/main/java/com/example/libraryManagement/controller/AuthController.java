package com.example.libraryManagement.controller;

import com.example.libraryManagement.model.User;
import com.example.libraryManagement.repository.UserRepository;
import com.example.libraryManagement.service.JwtService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        Optional<User> foundUser = userRepository.findByName(username);
        if (foundUser.isEmpty()) {
            model.addAttribute("error", "Incorrect username or password");
            return "login";
        }
        User user = foundUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Incorrect username or password");
            return "login";
        }

        String token = jwtService.generateToken(username);
        session.setAttribute("jwtToken", token);
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {
        if (userRepository.findByName(username).isPresent()) {
            model.addAttribute("error", "Username is already taken");
            return "register";
        }

        User user = new User(username, passwordEncoder.encode(password), email);
        userRepository.save(user);
        model.addAttribute("message", "Registration is successful! Sign in.");
        return "login";
    }
}
