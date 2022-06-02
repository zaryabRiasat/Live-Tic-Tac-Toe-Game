package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.react.modules.core.PermissionListener;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;

import timber.log.Timber;

public class OnlineGameActivity extends AppCompatActivity implements JitsiMeetActivityInterface
{

    FrameLayout lay_call;
    private JitsiMeetView view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);
        lay_call=findViewById(R.id.lay_call);
        setCall();



    }

    private void setCall()
    {
        view=new JitsiMeetView(this);
        JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                .setRoom("https://meet.jit.si/test123")
                .build();

        view.join(options);

        lay_call.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        registerForBroadcastMessages();


    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gamemenu,menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.itClose:
                Toast.makeText(OnlineGameActivity.this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itSound:
                //Toast.makeText(OnlineGameActivity.this, "", Toast.LENGTH_SHORT).show();

                break;
            case R.id.itLeave:
                Intent intent = new Intent(OnlineGameActivity.this,OnlineActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        JitsiMeetActivityDelegate.requestPermissions(this,permissions,requestCode,listener);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(
                this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        view.dispose();
        view = null;

        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }



    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final String[] permissions,
            final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetActivityDelegate.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JitsiMeetActivityDelegate.onHostPause(this);
        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };
    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
         */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }
    private void hangUp() {
        new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();


                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//
                        finish();
                        Log.e("usman", "hangUp: Closed" );

                    }
                });
            }
        }.run();




    }


    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
//                    Toast.makeText(MeetingActivity.this, ""+"Conference Joined with url%s"+ event.getData().get("url"), Toast.LENGTH_SHORT).show();
                    Log.e("usman", "onBroadcastReceived: "+ "Conference Joined with url%s"+ event.getData().get("url"));
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));

                    break;
                case PARTICIPANT_JOINED:
                    Log.e("usman", "onBroadcastReceived: "+"Participant joined%s"+ event.getData().get("name") );
//                    Toast.makeText(MeetingActivity.this, ""+"Participant joined%s"+event.getData().get("name"), Toast.LENGTH_SHORT).show();
//                    muteAudio();
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;
                case CONFERENCE_TERMINATED:
                    hangUp();

                    break;
                case READY_TO_CLOSE:
                    Log.e("usman", "onBroadcastReceived: Meeting hangup" );

                    break;


            }
        }
    }

}