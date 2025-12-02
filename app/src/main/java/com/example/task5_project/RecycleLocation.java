package com.example.task5_project;

public class RecycleLocation {
    public String name;
    public String address;
    public String description;
    public double lat;
    public double lng;
    public boolean expanded = false;

    public double distanceMeters;
    private String acceptedMaterials;


    public RecycleLocation(String name, String address, String description, String acceptedMaterials, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.distanceMeters = 0.0;
        this.acceptedMaterials = acceptedMaterials;
    }
    public String getAcceptedMaterials(){
        return acceptedMaterials;
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
}
