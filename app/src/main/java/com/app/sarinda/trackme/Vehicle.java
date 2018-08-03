package com.app.sarinda.trackme;



public class Vehicle  {
    private String vehi_model;
    private String vehi_num;
    private String insurance_date;
    private String licence_date;
    private String service_date;
    private String vehi_image;
    private String date;
    private Double distance;
    private Double Speed;
    private Double Latitude;
    private Double Longitude;
    private Double direction;
    private Double noofPersons;

    public Vehicle() {
    }

    public Vehicle(String vehi_model, String vehi_num, String insurance_date, String licence_date, String service_date, String vehi_image, String date, Double distance, Double speed, Double latitude, Double longitude, Double direction) {
        this.vehi_model = vehi_model;
        this.vehi_num = vehi_num;
        this.insurance_date = insurance_date;
        this.licence_date = licence_date;
        this.service_date = service_date;
        this.vehi_image = vehi_image;
        this.date = date;
        this.distance = distance;
        this.Speed = speed;
        Latitude = latitude;
        Longitude = longitude;
        this.direction = direction;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getVehi_model() {
        return vehi_model;
    }

    public void setVehi_model(String vehi_model) {
        this.vehi_model = vehi_model;
    }

    public String getVehi_num() {
        return vehi_num;
    }

    public void setVehi_num(String vehi_num) {
        this.vehi_num = vehi_num;
    }

    public String getInsurance_date() {
        return insurance_date;
    }

    public void setInsurance_date(String insurance_date) {
        this.insurance_date = insurance_date;
    }

    public String getLicence_date() {
        return licence_date;
    }

    public void setLicence_date(String licence_date) {
        this.licence_date = licence_date;
    }

    public String getService_date() {
        return service_date;
    }

    public void setService_date(String service_date) {
        this.service_date = service_date;
    }

    public String getVehi_image() {
        return vehi_image;
    }

    public void setVehi_image(String vehi_image) {
        this.vehi_image = vehi_image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getSpeed() {
        return Speed;
    }

    public void setSpeed(Double speed) {
        this.Speed = speed;
    }


    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }
}
