package com.mycompany.booking;

import com.mycompany.user.model.User;
import com.mycompany.user.util.JwtUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class BookingResource {

    @Inject
    BookingService bookingService;

    @POST
    @Path("/create")
    public Response createBooking(
            @HeaderParam("Authorization") String token,  // Get the token from the header
            @FormParam("classId") Long classId) {  // Get the classId from the request

        try {
            // Step 1: Validate the token is present
            if (token == null || token.isEmpty()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Missing or invalid token").build();
            }

            // Step 2: Extract email from the token
            String email;
            try {
                email = JwtUtil.extractEmailFromToken(token);  // Extract email from token
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid token").build();
            }

            // Step 3: Validate that classId is provided
            if (classId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Missing classId").build();
            }

            // Step 4: Call the booking service to handle the booking
            bookingService.createBooking(email, classId);

            return Response.ok("Booking created successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Booking creation failed").build();
        }
    }
}
