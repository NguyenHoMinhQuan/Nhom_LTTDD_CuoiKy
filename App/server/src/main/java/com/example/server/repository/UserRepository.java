package com.example.server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
