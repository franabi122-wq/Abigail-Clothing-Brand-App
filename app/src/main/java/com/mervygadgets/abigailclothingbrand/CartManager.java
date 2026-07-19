
package com.mervygadgets.abigailclothingbrand;

import com.mervygadgets.abigailclothingbrand.models.CartItem;
import com.mervygadgets.abigailclothingbrand.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    public void removeFromCart(int position) {
        cartItems.remove(position);
    }

    public void increaseQuantity(int position) {
        cartItems.get(position).setQuantity(cartItems.get(position).getQuantity() + 1);
    }

    public void decreaseQuantity(int position) {
        CartItem item = cartItems.get(position);
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            cartItems.remove(position);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getItemCount() {
        return cartItems.size();
    }
}