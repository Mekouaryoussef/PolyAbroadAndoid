package com.esisa.polyabroad.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esisa.polyabroad.R;
import com.esisa.polyabroad.models.ReviewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlertRVAdapter extends RecyclerView.Adapter<AlertHolder> {

    private List<ReviewModel> reviewList;

    public AlertRVAdapter(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public AlertHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.alert_items, viewGroup, false);

        return new AlertHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlertHolder viewHolder, int i) {
        ReviewModel review = reviewList.get(i);
        viewHolder.getStudent().setText(review.getStudent());
        viewHolder.getUniversity().setText(review.getUniversity());
        viewHolder.getDestination().setText(review.getDestination());
        viewHolder.getRatingBar().setRating((float) review.getRate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = review.getDate().format(formatter);
        viewHolder.getDates().setText(formattedString);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

