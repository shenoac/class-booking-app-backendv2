package com.mycompany.profiles;

import com.mycompany.user.model.User;
import com.mycompany.user.service.UserService;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ArtistProfileService {

    @Inject
    ArtistProfileRepository artistProfileRepository;  // Repository for managing profile persistence

    @Inject
    UserService userService;  // Service to retrieve user info based on email

    @Transactional
    public ArtistProfile createOrUpdateProfile(String email, String bio, String exhibitions) {
        // Find user by email
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find or create a new artist profile for the user
        ArtistProfile profile = artistProfileRepository.find("user", user).firstResult();
        if (profile == null) {
            profile = new ArtistProfile();  // Create new profile if not found
            profile.setUser(user);
        }

        // Update profile fields
        profile.setBio(bio);
        profile.setExhibitions(exhibitions);

        // Save or update profile in the database
        artistProfileRepository.persist(profile);
        return profile;
    }

    public ArtistProfile getProfileByEmail(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Retrieve the profile by user
        return artistProfileRepository.find("user", user).firstResult();
    }

    public ArtistProfile getProfileByArtistName(String artistName) {
        return artistProfileRepository.find("artistName", artistName).firstResult(); // No underscore, camelCase if the field is artistName
    }


}
