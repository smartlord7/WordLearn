package com.example.cmproject.util;

public class MarkerData {
    public String markerId;
    public String tier;
    public double latitude;
    public double longitude;
    public double score;
    public String owner;

    public MarkerData() {
        // Default constructor required for Firebase
    }

    public MarkerData(String markerId,String tier, double latitude, double longitude, double score, String owner) {
        this.markerId = markerId;
        this.tier = tier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.owner = owner;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}