package com.mycompany.payments;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StripeWebhookResource {

    @ConfigProperty(name = "stripe.webhook.secret")
    String webhookSecret;

    @POST
    public Response handleWebhook(String payload, @HeaderParam("Stripe-Signature") String signature) {
        try {

            Event event = Webhook.constructEvent(payload, signature, webhookSecret);


            switch (event.getType()) {
                case "payment_intent.succeeded":
                    System.out.println("PaymentIntent succeeded: " + event);

                    break;

                case "payment_intent.payment_failed":
                    System.out.println("PaymentIntent failed: " + event);

                    break;


                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }


            return Response.ok().build();
        } catch (Exception e) {

            System.out.println("Webhook error: " + e.getMessage());
            return Response.status(400).entity("Webhook error: " + e.getMessage()).build();
        }
    }
}
