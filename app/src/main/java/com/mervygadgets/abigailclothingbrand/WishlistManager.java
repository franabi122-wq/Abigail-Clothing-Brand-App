package com.mervygadgets.abigailclothingbrand;

import com.mervygadgets.abigailclothingbrand.models.Product;
import com.mervygadgets.abigailclothingbrand.models.WishlistItem;

import java.util.ArrayList;
import java.util.List;

public class WishlistManager {

    private static WishlistManager instance;
    private List<WishlistItem> wishlistItems = new ArrayList<>();

    private WishlistManager() {}

    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    public void addToWishlist(Product product) {
        for (WishlistItem item : wishlistItems) {
            if (item.getProduct().getId() == product.getId()) {
                return;
            }
        }
        wishlistItems.add(new WishlistItem(product));
    }

    public void removeFromWishlist(int position) {
        wishlistItems.remove(position);
    }

    public void clearWishlist() {
        wishlistItems.clear();
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public int getItemCount() {
        return wishlistItems.size();
    }
}
