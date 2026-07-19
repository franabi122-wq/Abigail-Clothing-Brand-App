package com.mervygadgets.abigailclothingbrand.models;

public class WishlistItem {

    private Product product;

    public WishlistItem(Product product) {
        this.product = product;
    }

    // Getter
    public Product getProduct() { return product; }

    // Setter
    public void setProduct(Product product) { this.product = product; }
}
