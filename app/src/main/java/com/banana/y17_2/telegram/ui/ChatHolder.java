package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.banana.y17_2.telegram.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView message;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        message = itemView.findViewById(R.id.message);
    }
}

