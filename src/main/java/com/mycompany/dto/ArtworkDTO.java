package com.mycompany.dto;

import jakarta.validation.constraints.NotBlank;

public class ArtworkDTO {

    @NotBlank(message = "Title is required")
    public String title;

    @NotBlank(message = "Description is required")
    public String description;

    @NotBlank(message = "Image URL is required")
    public String imageUrl;

    // Constructors, getters, and setters can be added as needed
}
