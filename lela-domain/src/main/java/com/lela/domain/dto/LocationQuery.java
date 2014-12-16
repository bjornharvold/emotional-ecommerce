/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Chris Tallent
 * Date: 11/8/11
 * Time: 11:41 AM
 * Responsibility:
 */
public class LocationQuery {

    /**
     * Latitude
     */
    private Float latitude;

    /**
     * Longitude
     */
    private Float longitude;

    /**
     * Radius
     */
    private Float radius;

    /**
     * IP Address if Lat/Long not available
     */
    private String ipAddress;

    /**
     * Zipcode if Lat/Long not available
     */
    private String zipcode;

    /**
     * City name
     */
    private String city;

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
