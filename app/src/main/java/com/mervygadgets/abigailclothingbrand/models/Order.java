package com.mervygadgets.abigailclothingbrand.models;

import com.google.gson.annotations.SerializedName;

public class Order {

    // Changed from primitive int to Integer object so it defaults to null
    @SerializedName("id")
    private Integer id = null;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("customer_phone")
    private String customerPhone;

    @SerializedName("address")
    private String address;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("total_amount")
    private double totalAmount;

    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("items")
    private String items;

    // Updated getter and setter to handle Integer object
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public String getAddress() { return address; }
    public String getLandmark() { return landmark; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getItems() { return items; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setAddress(String address) { this.address = address; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setItems(String items) { this.items = items; }
}