package com.banana.y17_2.telegram.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public class MainActivity extends AppCompatActivity implements Client.ResultHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelegramManager.getInstance().initialize(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TelegramManager.getInstance().setmResultHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TelegramManager.getInstance().removemResultHandler(this);
    }


    @Override
    public void onResult(TdApi.Object object) {
        if (object.getConstructor() == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
            final TdApi.AuthorizationState authorizationState = ((TdApi.UpdateAuthorizationState) object).authorizationState;
            if (authorizationState.getConstructor() == TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR) {
                showFragment(new EnteringPhoneFragment());
            } else if (authorizationState.getConstructor() == TdApi.AuthorizationStateWaitCode.CONSTRUCTOR) {
                showFragment(new EnteringPinFragment());
            } else if (authorizationState.getConstructor() == TdApi.AuthorizationStateReady.CONSTRUCTOR) {
                showFragment(new ChatsFragment());
            }
        }
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
