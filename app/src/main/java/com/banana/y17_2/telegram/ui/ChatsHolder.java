package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.banana.y17_2.telegram.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsHolder extends RecyclerView.ViewHolder {

    CircleImageView imageView;
    TextView name;
    TextView message;
    View itemView;

    public ChatsHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        imageView = itemView.findViewById(R.id.chat_image);
        name = itemView.findViewById(R.id.chat_name);
        message = itemView.findViewById(R.id.chat_mess);
    }
}
