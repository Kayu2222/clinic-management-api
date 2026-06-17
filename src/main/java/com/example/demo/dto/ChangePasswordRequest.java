package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for PATCH /api/staff/{id}/password.
 * Example: { "password": "newpass" }
 */
public class ChangePasswordRequest {

    @NotBlank(message = "password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
