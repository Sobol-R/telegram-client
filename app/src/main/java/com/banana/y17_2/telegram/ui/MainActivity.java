package com.banana.y17_2.telegram.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelegramManager.getInstance(this).initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
