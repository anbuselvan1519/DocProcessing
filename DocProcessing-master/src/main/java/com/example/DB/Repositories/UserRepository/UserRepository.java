package com.example.DB.Repositories.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.DB.Models.UserModel.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByEmail(String email);
}
