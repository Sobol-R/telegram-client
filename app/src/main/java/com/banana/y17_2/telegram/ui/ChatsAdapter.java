package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banana.y17_2.telegram.R;
import com.bumptech.glide.Glide;

import org.drinkless.td.libcore.telegram.TdApi;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsHolder> {

    MainActivity mainActivity;
    RecyclerView recyclerView;

    public ChatsAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ChatsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.chat_item, viewGroup, false);
        ChatsHolder chatsHolder = new ChatsHolder(view);
        return chatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsHolder chatsHolder, int i) {
        ChatsCache chatsCache = ChatsCache.getInstance();
        TdApi.Chat chat = chatsCache.mChats.get(i);
        TdApi.Message lastMessage = chat.lastMessage;

        chatsHolder.name.setText(chat.title);
        //chatsHolder.message.setText(lastMessage.content.toString());
//
//        Glide
//                .with(mainActivity)
//                .load(chat.photo.small.remote)
//                .apply(fitCenterTransform())
//                .apply(centerCropTransform())
//                .into(chatsHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return ChatsCache.getInstance().mChats.size();
    }
}
