/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.facebook;

/**
 * User: Chris Tallent
 * Date: 3/21/12
 * Time: 3:03 PM
 */
public class FacebookLocation {
    private String id;
    private String name;
    private String city;
    private String state;
    private String country;
    private String zip;
    private Float[] coordinates;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Float[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Float[] coordinates) {
        this.coordinates = coordinates;
    }
}
