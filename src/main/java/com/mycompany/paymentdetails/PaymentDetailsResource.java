package com.mycompany.paymentdetails;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/payment-details")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentDetailsResource {

    @Inject
    PaymentDetailsService paymentDetailsService;

    @GET
    @Path("/price/{bookingId}")
    public Response getClassPrice(@PathParam("bookingId") Long bookingId) {
        try {
            // Fetch the class price from PaymentDetailsService
            BigDecimal price = paymentDetailsService.getClassPrice(bookingId);
            return Response.ok(price).build();  // Return the price as a JSON response
        } catch (IllegalArgumentException e) {
            // Handle case where the booking is not found
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
