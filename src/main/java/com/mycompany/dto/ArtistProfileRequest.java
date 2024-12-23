package com.mycompany.dto;

public class ArtistProfileRequest {
    private String artistName;
    private String bio;
    private String exhibitions;
    private String education;


    // Getters and setters
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getExhibitions() {
        return exhibitions;
    }

    public void setExhibitions(String exhibitions) {
        this.exhibitions = exhibitions;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
