package com.banana.y17_2.telegram.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    RecyclerView recyclerView;
    ChatAdapter chatAdapter;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final long chatId = getArguments().getLong("EXTRA_CHAT_ID");
        chatAdapter = new ChatAdapter((MainActivity) getActivity(), chatId);
        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);
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
    public void onEvent(ChatsCache.MessageChangeEvent event) {
        chatAdapter.swap(event.getMessages());
    }

}
