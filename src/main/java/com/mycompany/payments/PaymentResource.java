package com.mycompany.payments;

import com.stripe.model.PaymentIntent;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentService paymentService;

    @POST
    @Path("/create")
    public Response createPayment(PaymentRequest request) {
        try {
            PaymentIntent intent = paymentService.createPaymentIntent(request.getAmount());
            return Response.ok(intent.toJson()).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error creating payment: " + e.getMessage()).build();
        }
    }
}
