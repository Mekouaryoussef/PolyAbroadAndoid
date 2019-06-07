package com.esisa.polyabroad.models;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class ReviewModel {

    private String id;
    private String student;
    private String university;
    private double rate;
    private String review;
    private String destination;
    private LocalDateTime date;
    private String email;

    public ReviewModel(String id, String student, String email, String university, double rate, String review, String destination, String date) {
        this.student = student;
        this.id = id;
        this.university = university;
        this.rate = rate;
        this.review = review;
        this.email = email;
        this.destination = destination;
        Instant instant = Instant.parse(date);
        this.date = LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getStudent() {
        return student;
    }

    public String getUniversity() {
        return university;
    }

    public double getRate() {
        return rate;
    }

    public String getReview() {
        return review;
    }

    public String getDestination() {
        return destination;
    }
}
