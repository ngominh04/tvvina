package com.tvvina.tvvina.service;

import com.tvvina.tvvina.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.tvvina.tvvina.domain.User> user = Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("không tồn tại người dùng")));


        return User.withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .roles(user.get().getRole()) // ví dụ: "USER" hoặc "ADMIN"
                .build();
    }

}
