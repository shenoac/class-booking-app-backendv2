package com.mycompany.user.controller;

import com.mycompany.user.dto.ArtworkDTO;
import com.mycompany.user.service.UserService;
import com.mycompany.user.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.container.ContainerRequestContext;

@Path("/artist")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArtistController {

    @Inject
    UserService userService;

    @POST
    @Path("/upload")
    public Response uploadArtwork(ArtworkDTO artworkDTO, @Context ContainerRequestContext requestContext) {
        String token = requestContext.getHeaderString("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token is missing or invalid").build();
        }

        token = token.substring(7); // Remove "Bearer " prefix

        try {
            Claims claims = JwtUtil.validateToken(token);
            String username = claims.getSubject();
            String userRole = userService.getUserRole(username); // Get user role from the database

            // Check if the user has the 'ARTIST' role
            if (!"ARTIST".equals(userRole)) {
                return Response.status(Response.Status.FORBIDDEN).entity("You do not have permission to access this resource").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        // Placeholder logic for uploading the artwork (e.g., saving it to a database)
        return Response.ok("Artwork uploaded successfully").build();
    }
}
