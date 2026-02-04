package com.example.DB.Controllers.AuthController;

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

    @PostMapping("/login")
    public User login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        return authService.login(email, password, session);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "Logged out successfully";
    }

    @GetMapping("/me")
    public User getCurrentUser(HttpSession session) {
        return authService.getCurrentUser(session);
    }
}
