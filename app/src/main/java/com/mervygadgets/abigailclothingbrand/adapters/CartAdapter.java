package com.mervygadgets.abigailclothingbrand.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onIncrease(int position);
        void onDecrease(int position);
        void onDelete(int position);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartActionListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.cartProductName.setText(item.getProduct().getName());
        holder.cartProductPrice.setText("GHS " + String.format("%.2f", item.getTotalPrice()));
        holder.qtyCount.setText(String.valueOf(item.getQuantity()));

        Glide.with(context)
                .load(item.getProduct().getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.cartProductImage);

        holder.increaseQty.setOnClickListener(v -> listener.onIncrease(position));
        holder.decreaseQty.setOnClickListener(v -> listener.onDecrease(position));
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateList(List<CartItem> newList) {
        cartItems = newList;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView cartProductImage;
        TextView cartProductName, cartProductPrice, qtyCount;
        android.widget.Button increaseQty, decreaseQty;
        ImageButton deleteBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            qtyCount = itemView.findViewById(R.id.qtyCount);
            increaseQty = itemView.findViewById(R.id.increaseQty);
            decreaseQty = itemView.findViewById(R.id.decreaseQty);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}