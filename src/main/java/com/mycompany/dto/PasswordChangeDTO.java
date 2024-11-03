package com.mycompany.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordChangeDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    // Getter for currentPassword
    public String getCurrentPassword() {
        return currentPassword;
    }

    // Setter for currentPassword
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    // Getter for newPassword
    public String getNewPassword() {
        return newPassword;
    }

    // Setter for newPassword
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
