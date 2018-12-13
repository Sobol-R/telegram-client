package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banana.y17_2.telegram.R;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatsHolder> {

    MainActivity mainActivity;
    RecyclerView recyclerView;

    List<TdApi.Message> mMessages = new ArrayList<>();

    public ChatAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ChatsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.message_item, viewGroup, false);
        ChatsHolder chatsHolder = new ChatsHolder(view);
        return chatsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsHolder chatsHolder, int i) {
//        final TdApi.Message message = mMessages.get(i);
//        chatsHolder.message.setText(message.content);
//        if (chatsCache.mChats.get(i).lastMessage != null &&
//                chatsCache.mChats.get(i).lastMessage.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
//            TdApi.MessageText messageText = (TdApi.MessageText) chatsCache.mChats.get(i).lastMessage.content;
//            chatsHolder.message.setText(messageText.text.text);
//        }

    }

    @Override
    public void onViewRecycled(@NonNull ChatsHolder holder) {
        super.onViewRecycled(holder);
        holder.message.setText(null);
    }

    @Override
    public int getItemCount() {
        return ChatsCache.getInstance().mChats.size();
    }
}
