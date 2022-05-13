package com.example.splash;

public class Visits {
    public String user;
    public String city;
    public String name;
    public String locationOnMap;
    public String date;
    public String status;
    public String tourist;
    public String description;

    public Visits() {
    }

    public Visits(String user, String city, String name, String locationOnMap, String date, String status, String tourist, String description) {
        this.user = user;
        this.city = city;
        this.name = name;
        this.locationOnMap = locationOnMap;
        this.date = date;
        this.status = status;
        this.tourist = tourist;
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getLocationOnMap() {
        return locationOnMap;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getTourist(){
        return tourist;
    }

    public String getDescription() {
        return description;
    }
}
