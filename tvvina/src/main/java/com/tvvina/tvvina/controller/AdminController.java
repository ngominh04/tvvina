package com.tvvina.tvvina.controller;

import com.tvvina.tvvina.domain.User;
import com.tvvina.tvvina.respository.UserRepository;
import com.tvvina.tvvina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    // get all user with role === USER
    @GetMapping("/getUserClient")
    public List<User> getUserClient() {
        List<User> users = userRepository.findAll();
        List<User> newUsers = new ArrayList<>();
        for (User user : users) {
            if (Objects.equals(user.getRole(), "USER")){
                newUsers.add(user);
            }
        }
        return newUsers;
    }
}
