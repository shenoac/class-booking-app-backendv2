package com.mycompany.profiles;

import com.mycompany.user.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistProfileRepository implements PanacheRepository<ArtistProfile> {
    // This repository will inherit basic CRUD methods from PanacheRepository

    // Additional custom queries can be defined if needed
}
