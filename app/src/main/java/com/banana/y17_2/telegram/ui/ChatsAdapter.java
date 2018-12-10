package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;
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

        chatsHolder.name.setText(chat.title);
        if (chatsCache.mChats.get(i).lastMessage != null &&
                chatsCache.mChats.get(i).lastMessage.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
            TdApi.MessageText messageText = (TdApi.MessageText) chatsCache.mChats.get(i).lastMessage.content;
            chatsHolder.message.setText(messageText.text.text);
        }

        if (chat.photo != null) {
            if (chat.photo.small.local.isDownloadingCompleted) {
                Glide
                .with(mainActivity)
                .load("file://" + chat.photo.small.local.path)
                .apply(fitCenterTransform())
                .apply(centerCropTransform())
                .into(chatsHolder.imageView);
            } else {
                TelegramManager.getInstance().downloadFile(chat.photo.small.id, 32);
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull ChatsHolder holder) {
        super.onViewRecycled(holder);
        holder.message.setText(null);
        holder.name.setText(null);
        holder.imageView.setImageBitmap(null);
    }

    @Override
    public int getItemCount() {
        return ChatsCache.getInstance().mChats.size();
    }
}
