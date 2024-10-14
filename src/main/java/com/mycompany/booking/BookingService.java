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
import java.util.stream.Collectors;

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

    public List<BookingDTO> getBookingsByUserEmail(String email) {
        // Find the user by email (if necessary)
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Fetch bookings by user ID and create BookingDTO to include class name
        return bookingRepository.find("user", user)
                .stream()
                .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getBookedClass().getClassName(),  // Include class name
                        booking.getBookingDate(),
                        booking.getStatus(),
                        booking.getPaymentStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteBooking(Long bookingId, String email) {
        // Find the user by email
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Find the booking by ID and ensure the user owns it
        Booking booking = bookingRepository.findById(bookingId);
        if (booking != null && booking.getUser().equals(user)) {
            bookingRepository.delete(booking);
            return true; // Deletion successful
        }

        return false; // Booking not found or user does not own the booking
    }


}

