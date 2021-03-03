package com.example.myapp;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView){
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
    }
}
