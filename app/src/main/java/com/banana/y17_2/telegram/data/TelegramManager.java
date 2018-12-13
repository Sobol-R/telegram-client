package com.banana.y17_2.telegram.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.banana.y17_2.telegram.BuildConfig;
import com.banana.y17_2.telegram.Constants;
import com.banana.y17_2.telegram.ui.ChatsCache;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TelegramManager implements Client.ExceptionHandler, Client.ResultHandler {

    private static TelegramManager sInstance;
    private Context mContext;
    private List<Client.ResultHandler> mResultHandler = new ArrayList<>();

    private Client mClient;

    public static TelegramManager getInstance() {
        if (sInstance == null) {
            sInstance = new TelegramManager();
        }
        return sInstance;
    }

    public void initialize(Context context) {
        mClient = Client.create(this, this, this);
        mContext = context;
        ChatsCache.getInstance().initialize();
    }

//  //  public void initialize() {
//        mClient = Client.create(this, this, this);
//    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                onUpdateAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
                break;
        }

        for (Client.ResultHandler resultHandler : mResultHandler) {
            resultHandler.onResult(object);
        }
    }

    @Override
    public void onException(Throwable e) {
        Log.e("TAG", "onException", e);
    }

    public void setmResultHandler(Client.ResultHandler resultHandler) {
        mResultHandler.add(resultHandler);
    }

    public void removemResultHandler(Client.ResultHandler resultHandler) {
        mResultHandler.remove(resultHandler);
    }

    public void downloadFile(int fileId, int priority) {
        mClient.send(new TdApi.DownloadFile(fileId, priority), this);
    }

    public void requestMessage(long chatId, long fromMessageId) {
        mClient.send(new TdApi.GetChatHistory(chatId, fromMessageId, 0, 42, false), this);
    }


    private void onUpdateAuthorizationState(TdApi.AuthorizationState authorizationState) {
        switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                // настраиваем TdLib
                final TdApi.TdlibParameters parameters = new TdApi.TdlibParameters();
                parameters.apiId = Constants.API_ID;
                parameters.apiHash = Constants.API_HASH;
                parameters.enableStorageOptimizer = true;
                parameters.useMessageDatabase = true; // разрешаем кэшировать chats и messages
                parameters.useFileDatabase = true; // разрешаем кэшировать файлы
                parameters.filesDirectory = mContext.getExternalFilesDir(null).getAbsolutePath() + "/"; // путь к файлам
                parameters.databaseDirectory = mContext.getFilesDir().getAbsolutePath() + "/"; // путь к БД
                parameters.systemVersion = Build.VERSION.RELEASE; // "7.1.2"
                parameters.deviceModel = Build.DEVICE; // "Xiaomi 4X"
                parameters.systemLanguageCode = Locale.getDefault().getLanguage(); // "en-GB"
                parameters.applicationVersion = BuildConfig.VERSION_NAME; // "1.0"
                mClient.send(new TdApi.SetTdlibParameters(parameters), this);
                break;
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                mClient.send(new TdApi.SetDatabaseEncryptionKey(), this);
                break;
        }
    }

    public void sendPhoneNumber(String phoneNumber) {
        mClient.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, false, false), this);
    }

    public void sendCode(String code) {
        mClient.send(new TdApi.CheckAuthenticationCode(code, null, null), this);
    }

    public void requestChats() {
        mClient.send(new TdApi.GetChats(Long.MAX_VALUE, 0, 42), this);
    }

}
