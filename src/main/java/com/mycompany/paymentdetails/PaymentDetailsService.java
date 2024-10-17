package com.mycompany.paymentdetails;

import com.mycompany.booking.BookingService;
import com.mycompany.booking.Booking;
import com.mycompany.classes.Class;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class PaymentDetailsService {

    @Inject
    BookingService bookingService;  // Use BookingService to fetch the booking details

    // Retrieve class price based on booking ID
    public BigDecimal getClassPrice(Long bookingId) {
        // Fetch the booking using bookingService
        Booking booking = bookingService.getBookingById(bookingId);

        // Ensure the booking exists
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found for ID: " + bookingId);
        }

        // Access the booked class and get the price
        Class bookedClass = booking.getBookedClass();  // Use getBookedClass to get the class entity
        return bookedClass.getPrice();  // Access the price from the class entity
    }
}
