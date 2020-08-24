package com.example.javaprojectweb;

public class Implements extends TeamMember{
    private int rating;

    public Implements(int rating) {
        this.rating = rating;
    }

    public Implements(int memberCode, String memberName, String memberMail, int rating) {
        super(memberCode, memberName, memberMail);
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
