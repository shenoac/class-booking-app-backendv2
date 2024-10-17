package com.mycompany.booking;

import com.mycompany.user.model.User;
import com.mycompany.user.util.JwtUtil;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class BookingResource {

    @Inject
    BookingService bookingService;

    @POST
    @Path("/create")
    public Response createBooking(
            @HeaderParam("Authorization") String token,
            @FormParam("classId") Long classId) {

        try {

            if (token == null || token.isEmpty()) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Missing or invalid token").build();
            }


            String email;
            try {
                email = JwtUtil.extractEmailFromToken(token);
            } catch (Exception e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid token").build();
            }


            if (classId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Missing classId").build();
            }


            bookingService.createBooking(email, classId);

            return Response.ok("Booking created successfully").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Booking creation failed").build();
        }
    }

    @GET
    @Path("/my-bookings")
    public Response getMyBookings(@HeaderParam("Authorization") String token) {
        try {

            String email = JwtUtil.extractEmailFromToken(token);


            List<BookingDTO> bookings = bookingService.getBookingsByUserEmail(email);


            return Response.ok(bookings).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or missing token").build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteBooking(@HeaderParam("Authorization") String token, @PathParam("id") Long bookingId) {
        try {

            String email = JwtUtil.extractEmailFromToken(token);


            boolean success = bookingService.deleteBooking(bookingId, email);

            if (success) {
                return Response.ok("Booking deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Booking not found or unauthorized").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or missing token").build();
        }
    }

}
