package com.tvvina.tvvina.service;

import com.tvvina.tvvina.domain.User;
import com.tvvina.tvvina.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(String username, String password,String phone,Integer isActive) {
        if (userRepository.existsByUsername(username)) {
            return "Tên đăng nhập đã tồn tại!";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setIsActive(isActive);
        user.setRole("USER");
        user.setCreateDate(LocalDate.now());
        userRepository.save(user);
        return "Đăng ký thành công!";
    }

}

