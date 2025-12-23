package com.example.server.controller;

import com.example.server.dto.UserDTO;
import com.example.server.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint API GET http://localhost:8080/api/users
    // Trả về danh sách UserDTO trực tiếp khi mở trình duyệt
    @GetMapping
    public List<UserDTO> getAllUsers() {
        // Lấy danh sách user và trả về luôn
        return userService.findAllUserDTOs();
    }
}
