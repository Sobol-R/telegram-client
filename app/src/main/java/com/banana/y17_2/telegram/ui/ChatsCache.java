package com.banana.y17_2.telegram.ui;

import com.banana.y17_2.telegram.data.TelegramManager;

import org.drinkless.td.libcore.telegram.Client.ResultHandler;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ChatsCache implements ResultHandler{

    private static ChatsCache sInstance;

    public final List<TdApi.Chat> mChats = new ArrayList<TdApi.Chat>();

    public static ChatsCache getInstance() {
        if (sInstance == null) {
            sInstance = new ChatsCache();
        }
        return sInstance;
    }

    public void initialize() {
        TelegramManager.getInstance().setmResultHandler(this);
    }


    @Override
    public void onResult(TdApi.Object object) {
        if (object.getConstructor() == TdApi.UpdateNewChat.CONSTRUCTOR) {
            mChats.add(((TdApi.UpdateNewChat) object).chat);

            EventBus.getDefault().post(new ChatsChangedEvent(mChats));
        } else if (object.getConstructor() == TdApi.UpdateChatLastMessage.CONSTRUCTOR) {
            for (TdApi.Chat chat : mChats) {
                if (chat.id == ((TdApi.UpdateChatLastMessage) object).chatId) {
                    chat.lastMessage = ((TdApi.UpdateChatLastMessage) object).lastMessage;
                    break;
                }

            }
        } else if (object.getConstructor() == TdApi.UpdateFile.CONSTRUCTOR) {
            for (TdApi.Chat chat : mChats) {
                if (chat.photo.small.id == ((TdApi.UpdateFile) object).file.id) {
                    chat.photo.small = ((TdApi.UpdateFile) object).file;
                    emitChatsHCngeEvent();
                    break;
                }
            }
        }
    }

    private void emitChatsHCngeEvent() {
        EventBus.getDefault().post(new ChatsChangedEvent(mChats));
    }

    public static class ChatsChangedEvent {
        private final List<TdApi.Chat> chats;

        public ChatsChangedEvent(List<TdApi.Chat> chats) {
            this.chats = chats;
        }
    }
}
