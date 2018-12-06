package com.banana.y17_2.telegram.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.banana.y17_2.telegram.R;
import com.banana.y17_2.telegram.data.TelegramManager;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnteringPinFragment extends Fragment {


    public EnteringPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entering_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText editText = view.findViewById(R.id.pin);
        final Button button = view.findViewById(R.id.send_pin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelegramManager.getInstance().sendCode(editText.getText().toString());
            }
        });

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtils.showKeyboard(getContext(), editText);
            }
        }, 200);
    }

}
