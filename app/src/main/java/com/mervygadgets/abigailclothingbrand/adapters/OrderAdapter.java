package com.mervygadgets.abigailclothingbrand.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.Order;
import com.mervygadgets.abigailclothingbrand.activities.TrackOrderActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderId.setText("Order #" + order.getCustomerName());
        holder.orderTotal.setText("GHS " + String.format("%.2f", order.getTotalAmount()));
        holder.orderAddress.setText(order.getAddress());

        if (holder.orderItems != null) {
            holder.orderItems.setText(order.getItems() != null ? order.getItems() : "Items Ordered");
        }

        String rawDate = order.getCreatedAt();
        if (rawDate != null && rawDate.length() >= 10) {
            holder.orderDate.setText(rawDate.substring(0, 10));
        } else {
            holder.orderDate.setText(rawDate != null ? rawDate : "Recent");
        }

        String status = order.getStatus();
        holder.orderStatus.setText(status);
        if (status != null) {
            switch (status.toLowerCase()) {
                case "delivered":
                    holder.orderStatus.setTextColor(0xFF00C853);
                    break;
                case "cancelled":
                    holder.orderStatus.setTextColor(0xFFFF4444);
                    break;
                default:
                    holder.orderStatus.setTextColor(0xFFFFA500);
                    break;
            }
        }

        //trackOrderBtn logic starts here
        // Replace your existing trackOrderBtn logic with this:
        holder.trackOrderBtn.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context, TrackOrderActivity.class);

            intent.putExtra("order_id", String.valueOf(order.getId()));
            intent.putExtra("order_status", order.getStatus());

            if(context instanceof com.mervygadgets.abigailclothingbrand.activities.AdminActivity){
                intent.putExtra("isAdmin", true);
            }

            context.startActivity(intent);
        });

       /* holder.trackOrderBtn.setOnClickListener(v -> {
            // 1. Define  status options
            // Check if the current screen is the Admin screen
            if (context instanceof com.mervygadgets.abigailclothingbrand.activities.AdminActivity) {
                // Admin behavior: Show the status-update dialog
                String[] options = {"Pending", "Shipped", "Delivered", "Cancelled"};
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Update Status for #" + order.getId())
                        .setItems(options, (dialog, which) -> {
                            ((com.mervygadgets.abigailclothingbrand.activities.AdminActivity) context)
                                    .updateOrderInSupabase(String.valueOf(order.getId()), options[which].toLowerCase());
                        })
                        .show();
            } else {
                // Customer behavior: Launch the TrackOrderActivity
                android.content.Intent intent = new android.content.Intent(context, TrackOrderActivity.class);
                intent.putExtra("order_id", String.valueOf(order.getId()));
                intent.putExtra("order_status", order.getStatus());
                context.startActivity(intent);
            }

        });*/
        //trackOrderBtn logic ends here

        holder.itemView.setOnClickListener(v -> listener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateList(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, orderDate, orderAddress, orderTotal, orderItems;
        android.widget.Button trackOrderBtn;//tracker button displayed here

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderItems = itemView.findViewById(R.id.orderItems);
            trackOrderBtn = itemView.findViewById(R.id.trackOrderBtn);
        }
    }
}