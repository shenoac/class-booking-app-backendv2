package com.mycompany.repository;

import com.mycompany.model.Class;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClassRepository implements PanacheRepository<Class> {
}

