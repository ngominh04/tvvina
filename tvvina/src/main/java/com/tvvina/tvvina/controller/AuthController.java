package com.tvvina.tvvina.controller;

import com.tvvina.tvvina.DTO.RegisterRequest;
import com.tvvina.tvvina.domain.User;
import com.tvvina.tvvina.respository.UserRepository;
import com.tvvina.tvvina.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request.getUsername(), request.getPassword(),request.getPhone(), request.getIsActive());
    }
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            response.put("error", "Sai tên đăng nhập hoặc mật khẩu");
            return response;
        }

        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        return response;
    }

}

