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

        System.out.println("Finding user with email: " + email);
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        System.out.println("Finding class with ID: " + classId);
        Class bookedClass = classRepository.findById(classId);
        if (bookedClass == null) {
            throw new IllegalArgumentException("Class not found");
        }

        // Check if the class is fully booked
        if (bookedClass.getBookedSlots() >= bookedClass.getMaxCapacity()) {
            throw new IllegalArgumentException("Class is fully booked");
        }

        System.out.println("Creating new booking...");
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookedClass(bookedClass);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("pending");
        booking.setPaymentStatus("pending");

        System.out.println("Saving booking...");
        bookingRepository.persist(booking);

        // Increment the booked slots after booking is created
        bookedClass.setBookedSlots(bookedClass.getBookedSlots() + 1);
        classRepository.persist(bookedClass);

        System.out.println("Booking created successfully!");
    }

    public List<BookingDTO> getBookingsByUserEmail(String email) {

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return bookingRepository.find("user", user)
                .stream()
                .map(booking -> new BookingDTO(
                        booking.getId(),
                        booking.getBookedClass().getClassName(),
                        booking.getBookingDate(),
                        booking.getStatus(),
                        booking.getPaymentStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteBooking(Long bookingId, String email) {

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        Booking booking = bookingRepository.findById(bookingId);
        if (booking != null && booking.getUser().equals(user)) {
            Class bookedClass = booking.getBookedClass();

            bookingRepository.delete(booking);

            // Decrement the booked slots after booking is deleted
            bookedClass.setBookedSlots(bookedClass.getBookedSlots() - 1);
            classRepository.persist(bookedClass);

            return true;
        }

        return false;
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
}

