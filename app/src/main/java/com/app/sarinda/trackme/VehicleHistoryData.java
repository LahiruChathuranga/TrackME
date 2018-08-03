package com.app.sarinda.trackme;

/**
 * Created by Chathura on 7/31/2018.
 */

public class VehicleHistoryData {
    private String speed;
    private String distance;
    private String date;

    public VehicleHistoryData() {
    }

    public VehicleHistoryData(String speed, String distance, String date) {
        this.speed = speed;
        this.distance = distance;
        this.date = date;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}