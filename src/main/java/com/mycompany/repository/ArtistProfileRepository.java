package com.mycompany.repository;

import com.mycompany.model.ArtistProfile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArtistProfileRepository implements PanacheRepository<ArtistProfile> {
    // This repository will inherit basic CRUD methods from PanacheRepository

    // Additional custom queries can be defined if needed
}
