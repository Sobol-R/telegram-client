package com.banana.y17_2.telegram.ui;

import com.banana.y17_2.telegram.data.TelegramManager;

import org.drinkless.td.libcore.telegram.Client.ResultHandler;
import org.drinkless.td.libcore.telegram.TdApi;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsCache implements ResultHandler{

    private static ChatsCache sInstance;

    public final List<TdApi.Chat> mChats = new ArrayList<TdApi.Chat>();

    public static ChatsCache getInstance() {
        if (sInstance == null) {
            sInstance = new ChatsCache();
        }
        return sInstance;
    }

    private Map <Long, Map<Long, TdApi.Message>> mMessages = new HashMap<>();

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
        } else if (object.getConstructor() == TdApi.Messages.CONSTRUCTOR) {
            final TdApi.Message[] newMessages = ((TdApi.Messages) object).messages;
            if (newMessages.length > 0) {
                long chatId = newMessages[0].chatId;
                Map <Long, TdApi.Message> oldMessages = mMessages.get(chatId);
                if (oldMessages == null) {
                    oldMessages = new HashMap<>();
                    mMessages.put(chatId, oldMessages);
                }

                for (TdApi.Message newMessage : newMessages) {
                    oldMessages.put(newMessage.id, newMessage);
                }

                final List<TdApi.Message> resultMessages = new ArrayList<>();
                for (TdApi.Message message : oldMessages.values()) {
                    resultMessages.add(message);
                }

                Collections.sort(resultMessages, new Comparator<TdApi.Message>() {
                    @Override
                    public int compare(TdApi.Message m1, TdApi.Message m2) {
                        return Long.compare(m2.id, m1.id);
                    }
                });

                emitMessagesChangedEvent(chatId, resultMessages);
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

    private void emitMessagesChangedEvent(long chatId, List<TdApi.Message> resultMessages) {
        EventBus.getDefault().post(new MessageChangeEvent(chatId, resultMessages));
    }

    public static class MessageChangeEvent {
        private final long chatId;
        private final List<TdApi.Message> messages;

        public MessageChangeEvent(long chatId, List<TdApi.Message> messages) {
            this.chatId = chatId;
            this.messages = messages;
        }

        public long getChatId() {
            return chatId;
        }

        public List<TdApi.Message> getMessages() {
            return messages;
        }
    }
}
