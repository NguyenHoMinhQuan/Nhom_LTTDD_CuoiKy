package com.example.server.controller;

import com.example.server.config.JwtUtils;
import com.example.server.dto.LoginRequest;
import com.example.server.dto.LoginResponse;
import com.example.server.dto.UserDTO;
import com.example.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils; // Inject JwtUtils để sinh mã Token

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        try {
            // 1. Thực hiện xác thực (Spring Security sẽ kiểm tra Username/Password)
            // Nếu Lombok lỗi, hãy đảm bảo LoginRequest có hàm getUsername() và
            // getPassword() viết tay
            // gọi authenticationManager kiểm tra username và password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            // 2. Nếu xác thực thành công, tạo chuỗi JWT Token từ Username
            String jwt = jwtUtils.generateJwtToken(authentication.getName());

            // 3. Lấy thông tin UserProfile an toàn (DTO)
            UserDTO userProfile = userService.findUserDTOByUsername(loginRequest.getUsername());

            // 4. Trả về phản hồi thành công kèm theo chuỗi TOKEN (Đảm bảo đủ 5 tham số)
            return ResponseEntity.ok(new LoginResponse(
                    loginRequest.getUsername(),
                    true,
                    "Đăng nhập thành công!",
                    jwt, // Tham số thứ 4: Token
                    userProfile)); // Tham số thứ 5: Profile

        } catch (Exception e) {
            // 5. Xử lý lỗi xác thực (Đảm bảo đủ 5 tham số cho LoginResponse)
            return ResponseEntity
                    .status(401)
                    .body(new LoginResponse(
                            loginRequest != null ? loginRequest.getUsername() : "Unknown",
                            false,
                            "Tên đăng nhập hoặc mật khẩu không hợp lệ.",
                            null, // Token là null khi thất bại
                            null)); // Profile là null khi thất bại
        }
    }
}