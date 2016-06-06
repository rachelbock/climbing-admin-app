package com.rage.clamberadmin;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Admin User Data class
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUser implements Parcelable{

    private String username;
    private String name;

    public AdminUser(){
        //no Arg constructor for jackson
    }
    public AdminUser(String username, String name){
        this.username = username;
        this.name = name;
    }

    protected AdminUser(Parcel in) {
        username = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdminUser> CREATOR = new Creator<AdminUser>() {
        @Override
        public AdminUser createFromParcel(Parcel in) {
            return new AdminUser(in);
        }

        @Override
        public AdminUser[] newArray(int size) {
            return new AdminUser[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
