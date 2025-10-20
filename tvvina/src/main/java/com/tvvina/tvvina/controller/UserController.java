package com.tvvina.tvvina.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @GetMapping("/api/user/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        // Lấy role đầu tiên (ví dụ: ROLE_ADMIN → ADMIN)
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "role", role
        ));
    }

    @GetMapping("/api/protected")
    public String getProtectedData() {
        return "Đây là nội dung chỉ dành cho người đã đăng nhập ✅";
    }
}
