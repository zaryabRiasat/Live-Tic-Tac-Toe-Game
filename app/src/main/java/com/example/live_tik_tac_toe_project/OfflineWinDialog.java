package com.example.live_tik_tac_toe_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class OfflineWinDialog extends Dialog {
    private String message;
    private OfflineGameActivity offlineGameActivity;
    public OfflineWinDialog(@NonNull Context context, String message, OfflineGameActivity offlineGameActivity) {
        super(context);
        this.message=message;
        this.offlineGameActivity=offlineGameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_win_dialog_layout);

        TextView messageTxt = (TextView) findViewById(R.id.etMessage);
        Button startAgain = (Button) findViewById(R.id.btnStartAgain);
        Button goToMain = (Button) findViewById(R.id.btnGoToMain);

        messageTxt.setText(message);

        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineGameActivity.restartMatch();
                dismiss();
            }
        });

        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineGameActivity.goToMain();
            }
        });

    }
}
