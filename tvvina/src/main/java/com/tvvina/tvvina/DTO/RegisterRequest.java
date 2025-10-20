package com.tvvina.tvvina.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String phone;
    private Integer isActive;
}

