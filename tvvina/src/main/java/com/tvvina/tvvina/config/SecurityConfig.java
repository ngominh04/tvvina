package com.tvvina.tvvina.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvvina.tvvina.service.CustomUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/check-username",
                                "/api/auth/register",
                                "/api/public/**",
                                "/api/recruit/**",
                                "/imageFiles/**",
                                "/images/**"
                        ).permitAll()
                        // 🔹 Chỉ ADMIN được vào các endpoint /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setContentType("application/json");
                            res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
                            res.setHeader("Access-Control-Allow-Credentials", "true");

                            String username = auth.getName();
                            String role = auth.getAuthorities().iterator().next().getAuthority();

                            Map<String, Object> body = new HashMap<>();
                            body.put("username", username);
                            body.put("role", role.replace("ROLE_", "")); // loại bỏ tiền tố ROLE_

                            new ObjectMapper().writeValue(res.getOutputStream(), body);
                            System.out.println("✅ Đăng nhập thành công: " + username + " (" + role + ")");
                        })
                        .failureHandler((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json");
                            Map<String, String> err = new HashMap<>();
                            err.put("error", "Sai tên đăng nhập hoặc mật khẩu");
                            new ObjectMapper().writeValue(res.getOutputStream(), err);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler((req, res, auth) -> {
                            System.out.println("📩 Nhận request logout từ frontend!");
                            if (auth != null) {
                                System.out.println("Người dùng đang đăng xuất: " + auth.getName());
                            } else {
                                System.out.println("⚠️ Không có auth context (chưa login hoặc cookie sai)");
                            }
                        })
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
                            res.setHeader("Access-Control-Allow-Credentials", "true");
                            System.out.println("✅ Đăng xuất thành công!");
                        })
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req, res, authEx) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                )
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
                    config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(java.util.List.of("*"));
                    config.setAllowCredentials(true); // ⚠️ Quan trọng
                    return config;
                }));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ⚡ Thêm AuthenticationManager để Spring biết cách xác thực
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}

