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
    private String name;
    private String gender;
    private String hairColor;
    private Double height;
    private String eyeColor;
    private Double shoeSize;
    private String extraFeatures;
    private String gpsLocation;
    private String dateAdded;
    private Boolean found;
    private Boolean visible;
    private String userID;
    private Integer age;
    private Boolean hasImage;

    /**
     * Empty default class constructor
     */
    public Person() {
        found = false;
        hasImage = false;
        setDateAdded();
    }

    /**
     * Constructor that takes in a persons name
     * @param name String person name
     */
    public Person(String ID, String name) {
        this.ID = ID;
        this.name = name;

        found = false;
        hasImage = false;
        setDateAdded();
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

    /**
     * Get the person's name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the person's name
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
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
     * Get the person's eye color
     * @return String eye color
     */
    public String getEyeColor() {
        return eyeColor;
    }

    /**
     * Set the person's eye color
     * @param eyeColor String eye color
     */
    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    /**
     * Get the person's shoe size
     * @return Double shoe size
     */
    public Double getShoeSize() {
        return shoeSize;
    }

    /**
     * Set the person's shoe size
     * @param shoeSize Double shoe size
     */
    public void setShoeSize(Double shoeSize) {
        this.shoeSize = shoeSize;
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

    /**
     * Get the date when the person was added
     * @return String date added
     */
    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded() {
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Calendar.getInstance().getTime());
        this.dateAdded = date;
    }

    /**
     * Set the date when the person was added
     * @param dateAdded
     */
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Get if the person has been found
     * @return Boolean found
     */
    public Boolean getFound() {
        return found;
    }

    /**
     * Set if the person has been found or not
     * @param found Boolean found
     */
    public void setFound(Boolean found) {
        this.found = found;
    }

    /**
     * Get if the person is visible
     * @return Boolean is visible
     */
    public Boolean getVisible() {
        return visible;
    }

    /**
     * Set if the person is visible
     * @param visible Boolean visible
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
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

    /**
     * Get the age of the person
     * @return Integer age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Set the age of the person
     * @param age Integer age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean hasImage() {
        return this.hasImage;
    }

    public void hasImage(Boolean flag) {
        this.hasImage = flag;
    }
}
