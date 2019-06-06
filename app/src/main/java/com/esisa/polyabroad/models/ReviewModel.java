package com.esisa.polyabroad.models;

public class ReviewModel {

    private String id;
    private String student;
    private String university;
    private double rate;
    private String review;
    private String destination;

    public ReviewModel(String id, String student, String university, double rate, String review, String destination) {
        this.student = student;
        this.id = id;
        this.university = university;
        this.rate = rate;
        this.review = review;
        this.destination = destination;
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
