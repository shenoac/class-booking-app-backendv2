package com.mycompany.booking;

import java.time.LocalDateTime;

public class BookingDTO {
    public Long id;
    public String className;
    public LocalDateTime bookingDate;
    public String status;
    public String paymentStatus;

    public BookingDTO(Long id, String className, LocalDateTime bookingDate, String status, String paymentStatus) {
        this.id = id;
        this.className = className;
        this.bookingDate = bookingDate;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }
}
