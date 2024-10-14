package com.mycompany.profiles;

import com.mycompany.user.util.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/artist-profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArtistProfileResource {

    @Inject
    ArtistProfileService artistProfileService;

    @POST
    @Path("/create-or-update")
    public Response createOrUpdateProfile(@HeaderParam("Authorization") String token, ArtistProfileRequest request) {
        try {
            String email = JwtUtil.extractEmailFromToken(token);  // Extract email from JWT token
            ArtistProfile profile = artistProfileService.createOrUpdateProfile(email, request.getBio(), request.getExhibitions());
            return Response.ok(profile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/view")
    public Response getProfile(@HeaderParam("Authorization") String token) {
        try {
            String email = JwtUtil.extractEmailFromToken(token);
            ArtistProfile profile = artistProfileService.getProfileByEmail(email);
            return Response.ok(profile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
