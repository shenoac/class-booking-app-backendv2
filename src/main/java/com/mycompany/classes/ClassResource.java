package com.mycompany.classes;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/classes")
public class ClassResource {

    @Inject
    ClassRepository classRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Class> getAllClasses() {
        return classRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClassById(@PathParam("id") Integer id) {
        Class classEntity = classRepository.findById(Long.valueOf(id));
        if (classEntity != null) {
            return Response.ok(classEntity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Class not found for ID: " + id)
                    .build();
        }
    }

}
