package com.example.DB.Services.AuthService;

import com.example.DB.DTO.RegisterRequest;
import com.example.DB.Models.UserModel.User;
import com.example.DB.Repositories.UserRepository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(RegisterRequest request, HttpSession session) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // (plain for now)
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        session.setAttribute("USER", user);
        session.setAttribute("USER_ID", user.getId());

        return user;
    }

    public User login(String email, String password, HttpSession session) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        session.setAttribute("USER", user);
        session.setAttribute("USER_ID", user.getId());

        return user;
    }

    public User getCurrentUser(HttpSession session) {

        Object userId = session.getAttribute("USER_ID");

        if (userId == null) {
            throw new RuntimeException("User not logged in");
        }

        return userRepository.findById((Long) userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
