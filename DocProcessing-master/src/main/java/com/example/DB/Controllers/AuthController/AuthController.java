package com.example.DB.Controllers.AuthController;

import com.example.DB.DTO.RegisterRequest;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Services.AuthService.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // -------- RAW JSON FORMAT INPUT ------------
    @PostMapping("/register")
    public User register(
            @RequestBody RegisterRequest request,
            HttpSession session
    ) {
        return authService.register(request, session);
    }

    @PostMapping("/login")
    public User login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        return authService.login(email, password, session);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "Logged out successfully";
    }

    @GetMapping("/me")
    public User getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("USER");
        if (user == null) {
            throw new IllegalStateException("User not logged in");
        }
        return user;
    }
}
