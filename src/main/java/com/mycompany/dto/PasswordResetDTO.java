package com.mycompany.dto;

import jakarta.validation.constraints.NotNull;

public class PasswordResetDTO {

    @NotNull
    private String token;

    @NotNull
    private String newPassword;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
