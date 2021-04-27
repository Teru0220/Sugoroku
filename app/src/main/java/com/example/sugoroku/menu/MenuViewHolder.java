package com.example.sugoroku.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sugoroku.R;

public class MenuViewHolder extends RecyclerView.ViewHolder{

    View base;
    ImageView imageView;
    TextView title,masuTotal;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        this.base = itemView;
        this.imageView = itemView.findViewById(R.id.imageView);
        this.title = itemView.findViewById(R.id.titleView);
        this.masuTotal = itemView.findViewById(R.id.masuTotal);
    }
}
