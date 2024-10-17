package com.mycompany.payments;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PaymentService {

    @ConfigProperty(name = "stripe.secret.key")
    String stripeSecretKey;

    @PostConstruct
    void initialize() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentIntent createPaymentIntent(long amount) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("payment_method_types", java.util.Arrays.asList("card"));

        return PaymentIntent.create(params);  // Stripe handles the rest
    }
}
