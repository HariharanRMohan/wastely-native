package com.bitp3453.bitis1g1.projectrequest;

public class Request {

    private String name;
    private String phonenumber;
    private String quantity;
    private String requestid;
    private String description;
    private double timestamp;
    private double coordinate;

    public Request(String name, String phonenumber, String quantity, String requestid, String description, double timestamp, double coordinate){
        this.name = name;
        this.phonenumber = phonenumber;
        this.quantity = quantity;
        this.requestid = requestid;
        this.description = description;
        this.timestamp = timestamp;
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(double coordinate) {
        this.coordinate = coordinate;
    }

}
