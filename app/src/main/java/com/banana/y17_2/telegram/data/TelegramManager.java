package com.banana.y17_2.telegram.data;

import android.content.Context;
import android.os.Build;

import com.banana.y17_2.telegram.BuildConfig;
import com.banana.y17_2.telegram.Constants;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.Locale;

public class TelegramManager implements Client.ExceptionHandler, Client.ResultHandler {

    private static TelegramManager sInstance;
    private Context mContext;

    private Client mClient;

    private TelegramManager(Context context) {
        mContext = context;
    }

    public static TelegramManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TelegramManager(context);
        }
        return sInstance;
    }

    public void initialize() {
        mClient = Client.create(this, this, this);
    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()) {
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                onUpdateAuthorizationState(((TdApi.UpdateAuthorizationState) object).authorizationState);
                break;
        }
    }

    @Override
    public void onException(Throwable e) {

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

}
