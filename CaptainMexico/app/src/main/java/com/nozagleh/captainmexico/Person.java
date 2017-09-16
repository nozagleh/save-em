package com.nozagleh.captainmexico;

import android.icu.util.Calendar;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arnar on 2017-08-29.
 */

@IgnoreExtraProperties
public class Person {
    private String ID;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String gender;
    private Double weight;
    private String hairColor;
    private Double height;
    private String extraFeatures;
    private String gpsLocation;
    private String nationality;
    private Date missingDate;
    private Integer found;
    private String userID;
    private Boolean hasImage;

    /**
     * Empty default class constructor
     */
    public Person() {
        found = 0;
        hasImage = false;
    }

    /**
     * Get the person's ID number
     * @return String person's id
     */
    public String get_ID() {
        return ID;
    }

    /**
     * Set the person's id number
     * @param ID String id
     */
    public void set_ID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Get the person's gender
     * @return String gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the person's gender
     * @param gender String gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * Get the person's hair color
     * @return String hair color
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * Set the person's hair color
     * @param hairColor String hair color
     */
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * Get the person's height
     * @return Double height
     */
    public Double getHeight() {
        return height;
    }

    /**
     * Set the person's height
     * @param height Double height
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * Get extra features, text with deeper description
     * @return String extra features
     */
    public String getExtraFeatures() {
        return extraFeatures;
    }

    /**
     * Set extra features, text with deeper description
     * @param extraFeatures String extra features
     */
    public void setExtraFeatures(String extraFeatures) {
        this.extraFeatures = extraFeatures;
    }

    /**
     * Get the persons gps location
     * @return String gps location
     */
    public String getGpsLocation() {
        return gpsLocation;
    }

    /**
     * Set the persons gps location
     * @param gpsLocation
     */
    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getMissingDate() {
        return missingDate;
    }

    public void setMissingDate(Date missingDate) {
        this.missingDate = missingDate;
    }

    /**
     * Get if the person has been found
     * @return Boolean found
     */
    public Integer getFound() {
        return found;
    }

    /**
     * Set if the person has been found or not
     * @param found Boolean found
     */
    public void setFound(Integer found) {
        this.found = found;
    }

    /**
     * Set the user tied to this person
     * @return String user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Get the user tied to this person
     * @param userID String user id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Boolean hasImage() {
        return this.hasImage;
    }

    public void hasImage(Boolean flag) {
        this.hasImage = flag;
    }
}
