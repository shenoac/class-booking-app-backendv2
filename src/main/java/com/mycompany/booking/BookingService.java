package com.mycompany.booking;

import com.mycompany.classes.Class;
import com.mycompany.classes.ClassRepository;
import com.mycompany.user.model.User;
import com.mycompany.user.service.UserService;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository bookingRepository;

    @Inject
    ClassRepository classRepository;

    @Inject
    UserService userService;

    @Transactional
    public void createBooking(String email, Long classId) {
        // Log that we're attempting to find the user
        System.out.println("Finding user with email: " + email);

        // Find the user
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        System.out.println("User found: ");

        // Log that we're attempting to find the class
        System.out.println("Finding class with ID: " + classId);

        // Find the class by its ID
        Class bookedClass = classRepository.findById(classId);
        if (bookedClass == null) {
            throw new IllegalArgumentException("Class not found");
        }
        System.out.println("Class found: " + bookedClass.getClassName());

        // Log that we're creating a new booking
        System.out.println("Creating new booking...");

        // Create a new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookedClass(bookedClass);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("pending");
        booking.setPaymentStatus("pending");

        // Log before persisting
        System.out.println("Saving booking...");

        // Save the booking
        bookingRepository.persist(booking);

        // Log success
        System.out.println("Booking created successfully!");
    }

    public List<Booking> getBookingsByUserEmail(String email) {
        // Find the user by email (if necessary)
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Fetch bookings by user ID
        return bookingRepository.find("user", user).list();
    }
}

