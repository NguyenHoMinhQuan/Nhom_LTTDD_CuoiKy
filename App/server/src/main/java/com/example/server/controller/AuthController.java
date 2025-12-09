package com.example.server.controller;

import com.example.server.dto.LoginRequest;
import com.example.server.dto.LoginResponse;
import com.example.server.dto.UserDTO;
import com.example.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            // 1. Thực hiện xác thực (gọi UserDetailsServiceImpl và BCrypt)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            // 2. Lấy UserDTO an toàn (sau khi xác thực thành công)
            UserDTO userProfile = userService.findUserDTOByUsername(loginRequest.getUsername());

            // 3. Trả về phản hồi thành công (Status 200 OK)
            return ResponseEntity.ok(new LoginResponse(
                    loginRequest.getUsername(),
                    true, // success = true
                    "Đăng nhập thành công!",
                    userProfile));

        } catch (Exception e) {
            // 4. Xử lý lỗi xác thực (Ví dụ: sai mật khẩu)
            return ResponseEntity
                    .status(401) // 401 Unauthorized
                    .body(new LoginResponse(
                            loginRequest.getUsername(),
                            false, // success = false
                            "Tên đăng nhập hoặc mật khẩu không hợp lệ.",
                            null));
        }
    }
}