package com.mervygadgets.abigailclothingbrand.models;
import com.google.gson.annotations.SerializedName;
public class Review {
    @SerializedName("id")
    private Integer id;

    @SerializedName("product_id")
    private Integer productId;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("rating")
    private Integer rating;

    @SerializedName("comment")
    private String comment;

    @SerializedName("created_at")
    private String createdAt;


    // Getters

    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    // Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
