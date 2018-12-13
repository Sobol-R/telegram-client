package com.banana.y17_2.telegram.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatFragment extends Fragment {

    public static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";

    public static ChatFragment newInstance(long chatId) {
        final ChatFragment fragment = new ChatFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(EXTRA_CHAT_ID, chatId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final long chatId = getArguments().getLong("EXTRA_CHAT_ID");
        TelegramManager.getInstance().requestMessage(chatId, 0);
    }
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatsChangeEvent(ChatsCache.ChatsChangedEvent event) {
        //adapter.notifyDataSetChanged();
    }

}
