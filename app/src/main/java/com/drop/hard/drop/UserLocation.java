package com.drop.hard.drop;

/**
 * Created by hard on 28/02/17.
 */
public class UserLocation {
    private String name;
    private String email;
    private String contact_number;
    private String uid;
    private double latitude;
    private double longitude;
    private Boolean public_visibilty;
    private String description;
    private String category;
    public UserLocation(){
    }
    public UserLocation(String name,String email,String contact_number,String uid, Boolean public_visibilty,double latitude,double longitude,String category,String description){
        this.latitude=latitude;
        this.name=name;
        this.contact_number=contact_number;
        this.email=email;
        this.description=description;
        this.longitude=longitude;
        this.category=category;
        this.uid=uid;
        this.public_visibilty=public_visibilty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getPublic_visibilty() {
        return public_visibilty;
    }

    public void setPublic_visibilty(Boolean public_visibilty) {
        this.public_visibilty = public_visibilty;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
