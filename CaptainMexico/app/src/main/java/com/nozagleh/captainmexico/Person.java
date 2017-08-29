package com.nozagleh.captainmexico;

import com.google.firebase.database.IgnoreExtraProperties;

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
    private String picUrl;

    /**
     * Empty default class constructor
     */
    public Person() {

    }

    /**
     * Constructor that takes in a persons name
     * @param name String person name
     */
    public Person(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void set_ID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Double getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(Double shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
