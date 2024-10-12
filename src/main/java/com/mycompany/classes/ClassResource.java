package com.mycompany.classes;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/classes")
public class ClassResource {

    @Inject
    ClassRepository classRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Class> getAllClasses() {
        return classRepository.listAll();  // Fetch all classes from the repository
    }
}
