package com.example.ankaref.DTO.Request.User;

import com.example.ankaref.Entities.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {

    private String name;
    private String surname;
    private String username;
    private String token;
    private Role role;
}