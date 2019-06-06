package com.esisa.polyabroad.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.esisa.polyabroad.R;

class AlertHolder extends RecyclerView.ViewHolder {
    private TextView student;
    private TextView destination;
    private TextView university;
    private RatingBar ratingBar;

    AlertHolder(@NonNull View itemView) {
        super(itemView);
        student = itemView.findViewById(R.id.student);
        destination = itemView.findViewById(R.id.destination);
        university = itemView.findViewById(R.id.university);
        ratingBar = itemView.findViewById(R.id.ratingBar);

    }

    TextView getStudent() {
        return student;
    }

    TextView getDestination() {
        return destination;
    }

    TextView getUniversity() {
        return university;
    }


    RatingBar getRatingBar() {
        return ratingBar;
    }
}
