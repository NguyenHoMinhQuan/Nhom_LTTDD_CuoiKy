package com.example.server.service;

import com.example.server.entity.User;
import com.example.server.dto.UserDTO;
import java.util.Optional;
import java.util.List;

public interface UserService {
    // Phương thức chính cho Spring Security
    Optional<User> findByUsername(String username);

    // Phương thức trả về DTO
    UserDTO findUserDTOByUsername(String username);

    List<User> findAllUsers();

    List<UserDTO> findAllUserDTOs(); // Dành cho API List
}