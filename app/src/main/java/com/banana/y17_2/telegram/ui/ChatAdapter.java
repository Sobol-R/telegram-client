package com.banana.y17_2.telegram.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

    MainActivity mainActivity;
    RecyclerView recyclerView;

    List<TdApi.Message> mMessages = new ArrayList<>();
    private final long mChatId;

    public ChatAdapter(MainActivity mainActivity, long chatId) {
        this.mainActivity = mainActivity;
        mChatId = chatId;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        View view = layoutInflater.inflate(R.layout.message_item, viewGroup, false);
        ChatHolder chatHolder = new ChatHolder(view);
        return chatHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder chatHolder, int i) {
        final TdApi.Message message = mMessages.get(i);
        if (message == null) {
            final long fromMessageId = mMessages.get(mMessages.size() - 2).id;
            TelegramManager.getInstance().requestMessage(mChatId, fromMessageId);
        } else {
            if (message.content.getConstructor() == TdApi.MessageText.CONSTRUCTOR) {
                chatHolder.message.setText(((TdApi.MessageText) message.content).text.text);
            }
        }
    }

    public void swap(@NonNull List<TdApi.Message> messages) {
        System.out.println("SWAAAAP");
        mMessages.clear();
        mMessages.addAll(messages);
        mMessages.add(null);
        notifyDataSetChanged();

    }

    @Override
    public void onViewRecycled(@NonNull ChatHolder holder) {
        super.onViewRecycled(holder);
        holder.message.setText(null);
    }

    @Override
    public int getItemCount() {
        return ChatsCache.getInstance().mChats.size();
    }
}
