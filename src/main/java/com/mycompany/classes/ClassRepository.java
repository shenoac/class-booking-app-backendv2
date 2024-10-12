package com.mycompany.classes;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<Class> {
    // Default methods provided by Panache are enough for now
}

