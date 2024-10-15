package com.mycompany.classes;

import com.mycompany.profiles.ArtistProfile;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "classes")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", referencedColumnName = "user_id", nullable = false)  // artist_id in classes references user_id in artist_profiles
    private ArtistProfile artistProfile;

    @Column(name = "class_name")
    private String className;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "schedule")
    private LocalDateTime schedule;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "booked_slots")
    private Integer bookedSlots;

    // Constructors, getters, and setters
    public Class() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArtistProfile getArtistProfile() {
        return artistProfile;
    }

    public void setArtistProfile(ArtistProfile artistProfile) {
        this.artistProfile = artistProfile;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(Integer bookedSlots) {
        this.bookedSlots = bookedSlots;
    }
}
