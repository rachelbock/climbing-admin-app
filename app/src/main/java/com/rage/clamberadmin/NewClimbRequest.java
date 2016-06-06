package com.rage.clamberadmin;

/**
 * Class to hold information for a new climb. Body for add climb post.
 */
public class NewClimbRequest {

    protected int rating;
    protected String type;
    protected String color;
    protected int wallId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWallId() {
        return wallId;
    }

    public void setWallId(int wallId) {
        this.wallId = wallId;
    }
}
