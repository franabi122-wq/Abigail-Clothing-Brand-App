package com.mervygadgets.abigailclothingbrand.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mervygadgets.abigailclothingbrand.R;
import com.mervygadgets.abigailclothingbrand.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;


    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }


    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     TextView verifiedBadge;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_review, parent, false);

        return new ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        Review review = reviewList.get(position);

        holder.reviewerName.setText(review.getCustomerName());

        holder.reviewComment.setText(review.getComment());
        int rating = review.getRating();

        holder.verifiedBadge.setVisibility(View.VISIBLE);


        // Display stars based on rating
       // int rating = review.getRating();

        //String stars = "";

        //for(int i = 0; i < rating; i++){
           // stars += "⭐";
       // }

        //holder.reviewRating.setText(stars);

        StringBuilder stars = new StringBuilder();

        for(int i = 0; i < 5; i++){

            if(i < rating){
                stars.append("★");
            }else{
                stars.append("☆");
            }

        }

        holder.reviewRating.setText(
                stars.toString() + " " + rating + "/5"
        );


        holder.reviewDate.setText(review.getCreatedAt());
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerName;
        TextView reviewRating;
        TextView reviewComment;
        TextView reviewDate;
        TextView verifiedBadge;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewRating = itemView.findViewById(R.id.reviewRating);
            reviewComment = itemView.findViewById(R.id.reviewComment);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            verifiedBadge = itemView.findViewById(R.id.verifiedBadge);
        }
    }
}