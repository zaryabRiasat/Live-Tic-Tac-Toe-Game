package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnlineWinDialog extends Dialog {

    private final String message;
    private final OnlineRunActivity mainActivity;
    TextView messageTV;
    Button startBtn;

    public OnlineWinDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
        this.mainActivity = ((OnlineRunActivity)context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_win_dialog);

        initialization();
        action();

        messageTV.setText(message);
    }

    private void action()
    {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                getContext().startActivity(new Intent(getContext(), PlayerNameOnline.class));
                getContext().startActivity(new Intent(getContext(), OnlineActivity.class));
                mainActivity.finish();
            }
        });
    }

    private void initialization()
    {
        messageTV = findViewById(R.id.messageTV);
        startBtn = findViewById(R.id.startNewBtn);
    }
}