package com.mervygadgets.abigailclothingbrand.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<String> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(String category, int position);
    }

    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String category = categories.get(position);
        holder.categoryName.setText(category);

        // Import this if needed: android.content.res.ColorStateList
        // Import this if needed: android.graphics.Color

        if (position == selectedPosition) {
            // 1. Text becomes beautiful Champagne Gold
            holder.categoryName.setTextColor(android.graphics.Color.parseColor("#D4AF37"));

            // 2. FORCE the background pill to become your deep charcoal luxury color
            holder.itemView.setBackgroundResource(R.drawable.category_bg_selected);
            holder.itemView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1E1E1E")));
        } else {
            // 1. Unselected text stays crisp white
            holder.categoryName.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));

            // 2. FORCE unselected background pill to become a pitch black / dark coal outline
            holder.itemView.setBackgroundResource(R.drawable.category_bg);
            holder.itemView.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#121212")));
        }

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(prev);
            notifyItemChanged(position);
            listener.onCategoryClick(category, position);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}