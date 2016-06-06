package com.rage.clamberadmin;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * Contains data on wall sections
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WallSection implements Parcelable {

    protected String name;
    protected int id;
    protected Date dateLastUpdated;
    protected boolean isTopout;
    protected int mainWallId;

    public WallSection(){
        //No arg constructor for Jackson
    }

    protected WallSection(Parcel in) {
        name = in.readString();
        id = in.readInt();
        isTopout = in.readByte() != 0;
        mainWallId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeByte((byte) (isTopout ? 1 : 0));
        dest.writeInt(mainWallId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WallSection> CREATOR = new Creator<WallSection>() {
        @Override
        public WallSection createFromParcel(Parcel in) {
            return new WallSection(in);
        }

        @Override
        public WallSection[] newArray(int size) {
            return new WallSection[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    public boolean isTopout() {
        return isTopout;
    }

    public void setTopout(boolean topout) {
        isTopout = topout;
    }

    public int getMainWallId() {
        return mainWallId;
    }

    public void setMainWallId(int mainWallId) {
        this.mainWallId = mainWallId;
    }
}
