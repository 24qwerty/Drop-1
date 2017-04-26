package com.drop.hard.drop;

import java.util.HashMap;

/**
 * Created by hard on 28/02/17.
 */
public class User {
    private String name;
    private String email;
    private String contact_number;
    private String uid;


    private HashMap<String, Object> dateCreated;
    private double latitude;
    private double longitude;
    public User(){}
    public User(String name,String email,String contact_number,double latitude,double longitude,String uid){
        this.name=name;
        this.contact_number=contact_number;
        this.email=email;
        this.latitude=latitude;
        this.longitude=longitude;
        this.uid=uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }


    public HashMap<String, Object> getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
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
}
