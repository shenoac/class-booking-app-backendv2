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
            // Extract email and role from token
            String email = JwtUtil.extractEmailFromToken(token);
            String role = JwtUtil.extractRoleFromToken(token);

            // Check if the user is an artist
            if (!"artist".equals(role)) {
                return Response.status(Response.Status.FORBIDDEN).entity("Only artists can update their profile").build();
            }

            // Update profile
            ArtistProfile profile = artistProfileService.createOrUpdateProfile(email, request.getArtistName(), request.getBio(), request.getExhibitions(), request.getEducation());
            return Response.ok(profile).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    @GET
    @Path("/view/{artistName}")
    public Response getProfileByArtistName(@PathParam("artistName") String artistName) {
        try {
            ArtistProfile profile = artistProfileService.getProfileByArtistName(artistName);
            if (profile == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Profile not found").build();
            }
            return Response.ok(profile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
