package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.live_tik_tac_toe_project.Adapters.userSpecificAdapter;
import com.facebook.react.modules.core.PermissionListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import timber.log.Timber;

public class OnlineRunActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private  JitsiMeetUserInfo jitsiMeetUserInfo;
    private ArrayList<userSpecificModel> messagesFromDB;
    private ArrayList<String> mesgIds;
    RecyclerView listOfMessage;
    Query query;
    long Ssec=0;
    CountDownTimer ctn;
    boolean isTurnApplied=false;
    boolean  isConterStarted=false;
    FirebaseRecyclerOptions<ChatMessage> options;
    FrameLayout lay_call;
    private JitsiMeetView view;
    private LinearLayout player1Layout, player2Layout;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private TextView player1TV, player2TV;

    String userEmail="";
    private final List<int[]> combinationsList = new ArrayList<>();
    private final List<String> doneBoxes = new ArrayList<>();

    private String playerUniqueId = "0";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private CardView lay_mesg_notify;
    private TextView txt_mesg_count;

    private boolean opponentFound = false;
    private boolean gamejoin=false;

    private String opponentUniqueId = "0";

    private String status = "matching";

    private String playerTurn = "";
    private String myName="";
    private String opoName="";
  private TextView timer;
    private String connectionId = "";
    String RecieverName="";

    private Button btnCall;

    ValueEventListener turnsEventListener, wonEventListener;
    private final String[] boxesSelectedBy = {"", "","","","","","","",""};
    private String rec_email="";
    private String sen_email="";
    private boolean ismeWin=false;

    private int mesgCount=0;
    private boolean isDialogShown=false;
    private boolean isGamejoin=false;
    String connectionUniqueId="";
    private boolean isLeaveDialogShown=false;
    private boolean isStartedGame=false;
    private boolean isAutoTurnApplied=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_run);
           TextView textView1=findViewById(R.id.chat);
            btnCall=findViewById(R.id.btnCall);
        lay_mesg_notify=findViewById(R.id.lay_mesg_notify);
        txt_mesg_count=findViewById(R.id.txt_mesg_count);
        timer=findViewById(R.id.timer);
        mesgIds=new ArrayList<>();
        messagesFromDB=new ArrayList<>();

        ctn=   new CountDownTimer(30000, 1000) {

            public void onTick(long duration) {
                //tTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext resource id
                // Duration
                isAutoTurnApplied=false;
                long Mmin = (duration / 1000) / 60;
                Ssec = (duration / 1000) % 60;

                if (isTurnApplied)
                {
                    cancel();
                    timer.setText("");
                    isTurnApplied=false;
                    isConterStarted=false;

                }
                else if(Ssec < 31) {
                    timer.setText("" + Ssec+" secs");

                }
            }

            public void onFinish() {
                timer.setText("");


                isConterStarted=false;
                if (opponentUniqueId!=null && (!opponentUniqueId.equals("")))
                {


                    int randPos=getRandomNumberBetween(1,10);
                    String pos=""+randPos;
                    boolean isNumOkay=false;
                    if (!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId))
                    {
                        isNumOkay=true;
                    }

                    while (!isNumOkay)
                    {
                        randPos=getRandomNumberBetween(1,10);
                        pos=""+randPos;
                        if (!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId))
                        {
                            isNumOkay=true;
                            break;
                        }

                    }

                    if (isNumOkay)
                    {
                        //Random Turn Here
                        if(!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId)){


                            databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue(pos);
                            databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                            playerTurn = opponentUniqueId;
                        }
                        playerTurn = opponentUniqueId ;
                        isTurnApplied=false;
                        isAutoTurnApplied=true;
                        applyPlayerTurn(playerTurn);


                    }



                }
                if (isAutoTurnApplied)
                {
                    reInitCounter();
                }
                else {
                    ctn.start();
                }

            }

        };

        if (mesgCount<=0)
        {
            lay_mesg_notify.setVisibility(View.GONE);
        }



        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("UserChat");

        sen_email=mAuth.getCurrentUser().getUid().toString();

        messagesFromDB=new ArrayList<userSpecificModel>();


        textView1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
            chat();
               }
           });
        player1Layout = findViewById(R.id.player1Layout);
        player2Layout = findViewById(R.id.player2Layout);


        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        image9 = findViewById(R.id.image9);

        player1TV = findViewById(R.id.player1TV);
        player2TV = findViewById(R.id.player2TV);

//        final String getPlayerName = getIntent().getStringExtra("playerName");
        SharedPreferences sharedPreferences = getSharedPreferences("user_data",MODE_PRIVATE);
        String getPlayerName = sharedPreferences.getString("Name","");
        String getPlayerEmail=sharedPreferences.getString("email","");
         myName=getPlayerName;

        jitsiMeetUserInfo=new JitsiMeetUserInfo();

        String url="https://www.kindpng.com/picc/m/78-786207_user-avatar-png-user-avatar-icon-png-transparent.png";
        try {
            jitsiMeetUserInfo.setAvatar(new URL(""+url));
            jitsiMeetUserInfo.setDisplayName(getPlayerName);
            jitsiMeetUserInfo.setEmail(getPlayerEmail);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //myName=getPlayerName;
       // Toast.makeText(this, ""+myName, Toast.LENGTH_SHORT).show();
        combinationsList.add(new int[]{0,1,2});
        combinationsList.add(new int[]{3,4,5});
        combinationsList.add(new int[]{6,7,8});
        combinationsList.add(new int[]{0,3,6});
        combinationsList.add(new int[]{1,4,7});
        combinationsList.add(new int[]{2,5,8});
        combinationsList.add(new int[]{2,4,6});
        combinationsList.add(new int[]{0,4,8});

        ProgressDialog progressDialog = new ProgressDialog(this);
      //  progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting for Opponent");
        progressDialog.show();

        playerUniqueId = String.valueOf(System.currentTimeMillis());

        player1TV.setText(getPlayerName);

        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!opponentFound){

                    if(snapshot.hasChildren()){

                        for(DataSnapshot connections : snapshot.getChildren()){

                            String conId = connections.getKey();
                            Log.e("ConnectionParentID",conId);
                            connectionUniqueId=conId;


                            int getPlayersCount = (int)connections.getChildrenCount();

                            if(status.equals("waiting")){

                                if(getPlayersCount == 2){

                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn(playerTurn);


                                    boolean playerFound = false;

                                    for(DataSnapshot players : connections.getChildren()){

                                        String getPlayerUniqueId = players.getKey();

                                        if(getPlayerUniqueId.equals(playerUniqueId)){
                                            playerFound = true;
                                        }
                                        else if(playerFound){

                                            if (playerTurn.equals(playerUniqueId))
                                            {

                                                isStartedGame=true;
                                                isConterStarted=true;
                                                ctn.start();
                                            }

                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            Log.e("opponent name",""+getOpponentPlayerName);

                                            opoName=getOpponentPlayerName;
                                          rec_email  = players.child("playermail").getValue(String.class);
                                            Log.e("opponent id",""+rec_email);

                                            //   Toast.makeText(OnlineRunActivity.this, "player mail"+rec_email, Toast.LENGTH_SHORT).show();
                                          //  Toast.makeText(OnlineRunActivity.this, "playername"+getOpponentPlayerName, Toast.LENGTH_SHORT).show();
                                            String mail=players.child(players.getKey()).child("playermail").getValue(String.class);
                                             String name=players.child(players.getKey()).child("player_name").getValue(String.class);
                                            opponentUniqueId = players.getKey();

                                            //TODO parse the email from opponentUniqueId node
//                                        String mail=connections.child(connections.getKey()).child(players.getKey()).child("player_email").getValue(String.class);

//                                            Toast.makeText(OnlineRunActivity.this, "Hello world"+opponentUniqueId, Toast.LENGTH_LONG).show();
                                         try {
                                             Log.e("mail adrees",mail);
                                         }
                                         catch (Exception e)
                                         {

                                         }


                                            player2TV.setText(getOpponentPlayerName);
                                            opoName=getOpponentPlayerName;
                                            connectionId = conId;
//                                            Toast.makeText(OnlineRunActivity.this, "connect id"+connectionId, Toast.LENGTH_LONG).show();
                                           Log.e("connect id",connectionId);
                                            opponentFound = true;
                                            if (opponentUniqueId!=null && (!opponentUniqueId.equals("")))
                                            {

                                            }

                                            isGamejoin=true;
                                            checkUserJoin();
                                            startJitsiMeet();

                                            databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                            databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                            databaseReference.child("connections").removeEventListener(this);

                                        }
                                    }
                                }
                            }

                            else{

                                if(getPlayersCount == 1){


                                    Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("player_name", getPlayerName);
                                    userInfo.put("playermail", getPlayerEmail);
                                    connections.child(playerUniqueId).getRef().setValue(userInfo);



//                                    connections.child(playerUniqueId).child("playermail").getRef().setValue(getPlayerEmail);
//                                    connections.child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
//                                 //   connections.child(playerUniqueId).child("playermail").getRef().setValue(getPlayerEmail);

//                                    Toast.makeText(OnlineRunActivity.this, "Email is "+getPlayerEmail, Toast.LENGTH_SHORT).show();
                                    for(DataSnapshot players : connections.getChildren()){

                                        String getOpponentName = players.child("player_name").getValue(String.class);
                                        rec_email = players.child("playermail").getValue(String.class);
                                        //RecieverName=getOpponentEmail;
                                        opponentUniqueId = players.getKey();

                                        if (opponentUniqueId!=null && (!opponentUniqueId.equals("")))
                                        {
//                                            isGamejoin=true;
                                            isStartedGame=true;

                                        }

                                        isGamejoin=true;
                                        checkUserJoin();
//                                        Toast.makeText(OnlineRunActivity.this, "reciever email"+""+rec_email, Toast.LENGTH_SHORT).show();
                                        playerTurn = opponentUniqueId ;
                                        applyPlayerTurn(playerTurn);

                                        player2TV.setText(getOpponentName);
                                        opoName=getOpponentName;
                                        connectionId = conId;
                                        opponentFound = true;

                                        databaseReference.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                        databaseReference.child("won").child(connectionId).addValueEventListener(wonEventListener);

                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }

                                        databaseReference.child("connections").removeEventListener(this);

                                        startJitsiMeet();
                                        break;
                                    }
                                }
                            }


                        }

                        if(!opponentFound && !status.equals("waiting")){

                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());
//                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("playermail").getRef().setValue(getPlayerEmail);
//
//                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);

//                            isGamejoin=false;
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("player_name", getPlayerName);
                            userInfo.put("playermail", getPlayerEmail);
                            snapshot.child(connectionUniqueId).child(playerUniqueId).getRef().setValue(userInfo);

                            status = "waiting";
                        }
                    }

                    else{

                        connectionUniqueId = String.valueOf(System.currentTimeMillis());

//                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("playermail").getRef().setValue(getPlayerEmail);
//
//                        snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);

                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("player_name", getPlayerName);
                        userInfo.put("playermail", getPlayerEmail);
                        snapshot.child(connectionUniqueId).child(playerUniqueId).getRef().setValue(userInfo);
                        status = "waiting";
//                        isGamejoin=false;


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if(dataSnapshot.getChildrenCount() == 2){

                        final int getBoxPosition = Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));

                        final String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);

                        if(!doneBoxes.contains(String.valueOf(getBoxPosition))){

                            doneBoxes.add(String.valueOf(getBoxPosition));

                            if(getBoxPosition == 1){
                                selectBox(image1, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 2){
                                selectBox(image2, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 3){
                                selectBox(image3, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 4){
                                selectBox(image4, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 5){
                                selectBox(image5, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 6){
                                selectBox(image6, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 7){
                                selectBox(image7, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 8){
                                selectBox(image8, getBoxPosition, getPlayerId);
                            }
                            else if(getBoxPosition == 9){
                                selectBox(image9, getBoxPosition, getPlayerId);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("player_id")){

                    String getWinPlayerId = snapshot.child("player_id").getValue(String.class);

                    final OnlineWinDialog winDialog;

                    if(getWinPlayerId.equals(playerUniqueId)){

                        ismeWin=true;
                        isDialogShown=true;
                        winDialog = new OnlineWinDialog(OnlineRunActivity.this, "You won the game");

                   String email_value=snapshot.child(getWinPlayerId).child("playermail").getValue(String.class);
                   Log.e("even_mail",""+email_value);

                   //myName=player1TV.getText().toString();

                    }

                    else{

                      //  myName=opoName;
                        ismeWin=false;
                        isDialogShown=true;
                        winDialog = new OnlineWinDialog(OnlineRunActivity.this, "Opponent won the game");
                    }
                   /* if((doneBoxes.size() == 9 && isDialogShown) == true){

                        final WinDialog winDialog1 = new WinDialog(OnlineRunActivity.this, "It is a Draw!");
                        winDialog1.setCancelable(false);
                        winDialog1.show();
                    }*/

                    winDialog.setCancelable(false);
                    winDialog.show();

                    databaseReference.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    databaseReference.child("won").child(connectionId).removeEventListener(wonEventListener);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("1") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("1");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("2") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("2");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("3") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("3");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("4") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("4");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("5") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("5");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("6") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("6");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("7") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("7");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("8") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("8");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });

        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!doneBoxes.contains("9") && playerTurn.equals(playerUniqueId)){
                    ((ImageView)v).setImageResource(R.drawable.x);

                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue("9");
                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                    playerTurn = opponentUniqueId;
                }
            }
        });


        lay_call=findViewById(R.id.lay_call);
//        setCall();
    }


    private void applyPlayerTurn(String playerUniqueId2){

        if(playerUniqueId2.equals(playerUniqueId)){
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);

            isConterStarted=false;
          //  Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show();
            startCounterFunc();
        }
        else{
            player2Layout.setBackgroundResource(R.drawable.round_back_dark_blue_stroke);
            player1Layout.setBackgroundResource(R.drawable.round_back_dark_blue_20);
        }
    }

    private void selectBox(ImageView imageView, int selectedBoxPosition, String selectedByPlayer){

        boxesSelectedBy[selectedBoxPosition - 1] = selectedByPlayer;

        if(selectedByPlayer.equals(playerUniqueId)){
            imageView.setImageResource(R.drawable.x);
            playerTurn = opponentUniqueId;
            isTurnApplied=true;
            isConterStarted=false;
        }
        else{
            imageView.setImageResource(R.drawable.o);
            playerTurn = playerUniqueId;
        }

        applyPlayerTurn(playerTurn);





        if(checkPlayerWin(selectedByPlayer)){

         // if the player win his score will be 100

//            if (!isDialogShown)
//            {
//                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
//                final OnlineWinDialog  winDialog = new OnlineWinDialog(OnlineRunActivity.this, "It is a Draw!");
//                winDialog.setCancelable(false);
//                winDialog.show();
//            }
//            else
//            {
//                Toast.makeText(this, "World", Toast.LENGTH_SHORT).show();
//            }
            int result = 100;
            Map<String, Object> userInfo1 = new HashMap<>();
            userInfo1.put("player_id", selectedByPlayer);
         if (ismeWin=false)
            {
//                Toast.makeText(this, "myname"+myName, Toast.LENGTH_SHORT).show();
                userInfo1.put("player_name", myName);
            }
            else if (ismeWin=true)
            {
//                Toast.makeText(this, "oponame"+opoName, Toast.LENGTH_SHORT).show();

                userInfo1.put("player_name", opoName);
            }
            userInfo1.put("score", result);

            databaseReference.child("won").child(connectionId).getRef().setValue(userInfo1); //.child("player_id").setValue(selectedByPlayer);
        }


        if(doneBoxes.size() >= 9  ){


            final OnlineWinDialog  winDialog = new OnlineWinDialog(OnlineRunActivity.this, "It is a Draw!");
            winDialog.setCancelable(false);
            winDialog.show();
            String  test="";
        }

    }

    private boolean checkPlayerWin(String playerId){

        boolean isPlayerWon = false;

        for(int i = 0; i < combinationsList.size(); i++){

            final int[] combination = combinationsList.get(i);

            if(boxesSelectedBy[combination[0]].equals(playerId) &&
                    boxesSelectedBy[combination[1]].equals(playerId) &&
                    boxesSelectedBy[combination[2]].equals(playerId)){
                isPlayerWon = true;
            }
        }

        return isPlayerWon;
    }

int callState=0;
    private void startJitsiMeet() {

        if (callState==0)
        {
            btnCall.setText("Call Now");
        }
        else
        {
            btnCall.setText("End Call");
        }
        if (!connectionId.trim().equals(""))
        {
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callState==0)
                    {
                        setCall();
                    }
                    else
                    {
                        hangUp();
                    }

                }
            });

        }

    }

    private void setCall()
    {
//        view=new JitsiMeetView(this);
        String roomId="https://meet.jit.si/game_meet"+connectionId;
        JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder()
                .setRoom(roomId)
                .setAudioMuted(true)
                .setVideoMuted(true)
                .setAudioOnly(false)

                .setFeatureFlag("kick-out.enabled", false)
                .setFeatureFlag("invite.enabled", false)
                .setFeatureFlag("help.enabled", false)
                .setFeatureFlag("meeting-password.enabled", false)
                .setFeatureFlag("recording.enabled", false)
                .setFeatureFlag("overflow-menu.enabled",false)
//                        .setFeatureFlag("raise-hand.enabled", "disabled")
                .setFeatureFlag("raise-hand.enabled", false)
                .setFeatureFlag("notifications.enabled", false)
                .setFeatureFlag("reactions.enabled", false)
                .setFeatureFlag("chat.enabled", false)

//                .setFeatureFlag("lobby-mode.enabled", lobbyMode)
//                                .setSubject("My test Meeting")
                .setSubject("Connected" )
                .setFeatureFlag("add-people.enabled", false)
                .setFeatureFlag("security-options.enabled", false)
//                        .setFeatureFlag("security-options.enabled", "disabled")


                .setFeatureFlag("pip.enabled","enabled")
//                                .setFeatureFlag("android.screensharing.enabled","disabled")
//                                    .setWelcomePageEnabled(false)
//                                .setConfigOverride("requireDisplayName", true)
                .setUserInfo(jitsiMeetUserInfo)
                .build();

        JitsiMeetActivity.launch(OnlineRunActivity.this,options);
//        view.join(options);

//        lay_call.addView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
        registerForBroadcastMessages();


//        hangUp();



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pipActiveMode()
    {
        Display d = getWindowManager()
                .getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int width = p.x;
        int height = p.y;

        Rational ratio
                = new Rational(width, height);
        PictureInPictureParams.Builder
                pip_Builder
                = new PictureInPictureParams
                .Builder();
        pip_Builder.setAspectRatio(ratio).build();
        enterPictureInPictureMode(pip_Builder.build());
    }




    @Override
    protected void onStart() {
        super.onStart();
        String child="";


        if (mDatabase!=null)
        {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    for(DataSnapshot postSnapshots: snapshot.getChildren())
                    {
                        userSpecificModel userModel=postSnapshots.getValue(userSpecificModel.class);

                        String senderEmail=userModel.getSender();
                        String recieverEmail=userModel.getReciever();
                        Log.e("reciever email","hh: "+recieverEmail);
                        Log.e("sender email","hh: "+senderEmail);
                        if (sen_email!=null && recieverEmail!=null && senderEmail!=null && rec_email!=null)
                        {
                            if ((senderEmail.equals(sen_email)) || (recieverEmail.equals(sen_email)))
                            {
                                if (recieverEmail.equals(rec_email) || senderEmail.equals(rec_email))
                                {
                                    if (!mesgIds.contains(userModel.getMessageId()))
                                    {
                                        if (userModel.getReadStatus().equalsIgnoreCase("0"))
                                        {

                                            String mesg_rcr=userModel.getReciever();
                                            String myId=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                                            if (mesg_rcr.equalsIgnoreCase(myId))
                                            {
                                                mesgCount++;
                                            }

                                        }
                                        userModel.setReadStatus("1");

                                        messagesFromDB.add(userModel);
                                        mesgIds.add(userModel.getMessageId());
                                    }



                                }



                            }
                        }





                    }

                   if (mesgCount<=0)
                   {

                       lay_mesg_notify.setVisibility(View.GONE);
                   }
                   else
                   {
                       lay_mesg_notify.setVisibility(View.VISIBLE);
                       txt_mesg_count.setText(""+mesgCount);
                   }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void ClearChat()
    {

        String currentID = mAuth.getCurrentUser().getUid().toString();
        String mEmail=rec_email;

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    userSpecificModel model=snapshot1.getValue(userSpecificModel.class);

                    String key=snapshot1.getKey();

                    if (model.getMessageUser().equalsIgnoreCase(mEmail) || model.getSender().equalsIgnoreCase(currentID))
                    {

                        try {
                            mDatabase.child(key).removeValue();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onDestroy() {



            ClearChat();
            hangUp();
//        view.dispose();
//        view = null;



//        JitsiMeetActivityDelegate.onHostDestroy(this);
            Log.e("usman", "onDestroy: hello" );
            super.onDestroy();
        //}

    }

/*    @Override
    public void onNewIntent(Intent intent) {

        JitsiMeetActivityDelegate.onNewIntent(intent);
        super.onNewIntent(intent);
    }*/



//    @Override
//    public void onRequestPermissionsResult(
//            final int requestCode,
//            final String[] permissions,
//            final int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void checkUserJoin()
    {
        if (databaseReference!=null) {
            databaseReference.child("connections").child(connectionUniqueId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot snapshot12: snapshot.getChildren())
//                    {
//                        Toast.makeText(OnlineRunActivity.this, "size: "+snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();

                        if (isGamejoin ) {


//                            Toast.makeText(OnlineRunActivity.this, "Hi: "+snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();

                            if ((snapshot.getChildrenCount() < 2)) {
                                if (!isLeaveDialogShown) {


                                    Context mcontext= OnlineRunActivity.this;

                                    if (mcontext!=null && (!((Activity)mcontext).isFinishing() )) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineRunActivity.this);
                                        builder.setMessage("Your opponent has left the match!")
                                                .setCancelable(false)
                                                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        databaseReference.child("connections").child(connectionUniqueId)
                                                                .child(playerUniqueId)
                                                                .setValue(null);
                                                        finishAffinity();
                                                        startActivity(new Intent(OnlineRunActivity.this, OnlineActivity.class));

                                                    }
                                                });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        isLeaveDialogShown = true;

                                        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                isLeaveDialogShown = false;
                                            }
                                        });

                                    }

                                }
                            }

                        }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    protected void onResume() {
//        checkUserJoin();

//        JitsiMeetActivityDelegate.onHostResume(this);
        super.onResume();




    }



    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("usman", "onReceive: hi" );
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

        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();


        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//
//                        finish();
        Log.e("usman", "hangUp: Closed" );



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onBroadcastReceived(Intent intent) {
        Log.e("usman", "onBroadcastReceived: A" );
        if (intent != null) {

            BroadcastEvent event = new BroadcastEvent(intent);
            Log.e("usman", "onBroadcastReceived: B"+event.getType().toString() );

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    callState=1;
                    btnCall.setText("End Call");
//                    Toast.makeText(MeetingActivity.this, ""+"Conference Joined with url%s"+ event.getData().get("url"), Toast.LENGTH_SHORT).show();
                    Log.e("usman", "onBroadcastReceived: "+ "Conference Joined with url%s"+ event.getData().get("url"));
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));

//                    pipActiveMode();
                    break;
                case PARTICIPANT_JOINED:
                    Log.e("usman", "onBroadcastReceived: "+"Participant joined%s"+ event.getData().get("name") );
//                    Toast.makeText(MeetingActivity.this, ""+"Participant joined%s"+event.getData().get("name"), Toast.LENGTH_SHORT).show();
//                    muteAudio();
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;
                case CONFERENCE_TERMINATED:
                    Log.e("usman", "onBroadcastReceived: Meeting terminated" );
                    //hangUp();
                    callState=0;
                    btnCall.setText("Call Now");

                    break;
                case READY_TO_CLOSE:
                    callState=0;
                    btnCall.setText("Call Now");
                    Log.e("usman", "onBroadcastReceived: Meeting hangup" );

                    break;



            }
        }
    }



    public void chat()
    {
        final Dialog dialog5 = new Dialog(OnlineRunActivity.this);
        dialog5.setContentView(R.layout.chat_design);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 1.0);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 1.0);

        dialog5.getWindow().setLayout(width, height);
        ImageView ok = (ImageView) dialog5.findViewById(R.id.submit_button);
        EditText messageTxt = dialog5.findViewById(R.id.emojicon_edit_text);

        Button CloseDialog = dialog5.findViewById(R.id.closeDialog);

        RecyclerView recyclerView = dialog5.findViewById(R.id.list_of_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(OnlineRunActivity.this));

        query = FirebaseDatabase.getInstance().getReference("UserChat");
        options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query,ChatMessage.class)
                .build();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=mAuth.getCurrentUser().getEmail().toString();
                //Toast.makeText(MessagesActivity.this, userEmail, Toast.LENGTH_SHORT).show();

                String messageId = mDatabase.push().getKey();
                String currentID = mAuth.getCurrentUser().getUid().toString();

//                Toast.makeText(OnlineRunActivity.this, ""+rec_email, Toast.LENGTH_SHORT).show();


                userSpecificModel userModel=new userSpecificModel(messageTxt.getText().toString(),
                        userEmail,messageId,currentID,rec_email);



                mDatabase.push().setValue(userModel);
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Populate RecyclerView Here


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        //Reset RecyclerView Here

                    }
                });



                // displayChatMessage();


                messageTxt.setText("");
                messageTxt.requestFocus();
            }
        });

        mesgCount=0;
        lay_mesg_notify.setVisibility(View.GONE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshots: dataSnapshot.getChildren())
                {
                    userSpecificModel userModel=postSnapshots.getValue(userSpecificModel.class);

                    String senderEmail=userModel.getSender();
                    String recieverEmail=userModel.getReciever();
                    Log.e("reciever email","hh: "+recieverEmail);
                    Log.e("sender email","hh: "+senderEmail);
                    if (sen_email!=null && recieverEmail!=null && senderEmail!=null && rec_email!=null)
                    {
                        if ((senderEmail.equals(sen_email)) || (recieverEmail.equals(sen_email)))
                        {
                            if (recieverEmail.equals(rec_email) || senderEmail.equals(rec_email))
                            {
                                if (!mesgIds.contains(userModel.getMessageId()))
                                {
                                    userModel.setReadStatus("1");

                                    messagesFromDB.add(userModel);
                                    mesgIds.add(userModel.getMessageId());

                                    mesgCount=0;
                                    lay_mesg_notify.setVisibility(View.GONE);
                                }



                            }



                        }
                    }





                }


                userEmail=mAuth.getCurrentUser().getEmail();

                userSpecificAdapter adapter=new userSpecificAdapter(messagesFromDB,userEmail,OnlineRunActivity.this);
                //MessageAdapter adapter=new MessageAdapter(messagesFromDB,userEmail,MessageToSpecific.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(OnlineRunActivity.this));

                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(messagesFromDB.size()-1);
                adapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        CloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog5.dismiss();
            }
        });


        if (mesgCount>0)
        {
            lay_mesg_notify.setVisibility(View.VISIBLE);
            txt_mesg_count.setText(""+mesgCount);
        }
        else
        {
            lay_mesg_notify.setVisibility(View.GONE);
        }
        dialog5.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onStart();
            }
        });

        dialog5.show();
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, ""+connectionUniqueId, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(OnlineRunActivity.this);
        builder.setMessage("Are you sure you want to leave the game?")
                .setCancelable(false)
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseReference.child("connections").child(connectionUniqueId)
                                .child(playerUniqueId)
                                .setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                Toast.makeText(OnlineRunActivity.this, "Node deleted "+playerUniqueId, Toast.LENGTH_LONG).show();
                            }
                        });
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void startCounterFunc()
    {
         /*ctn=   new CountDownTimer(30000, 1000) {

            public void onTick(long duration) {
                //tTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext resource id
                // Duration
                long Mmin = (duration / 1000) / 60;
                Ssec = (duration / 1000) % 60;

                if (isTurnApplied)
                {
                    cancel();
                    timer.setText("");
                    isTurnApplied=false;
                }
                if (Ssec < 31) {
                    timer.setText("" + Ssec+" secs");
                }
            }

            public void onFinish() {
                timer.setText("");

            }

        };*/


        if(!isConterStarted)
        {
       //     Toast.makeText(this, "Hello yyy", Toast.LENGTH_SHORT).show();

            if (isStartedGame)
            {
             //   Toast.makeText(this, "Hello worldzzz", Toast.LENGTH_SHORT).show();
                isConterStarted=true;
                if (isAutoTurnApplied)
                {
                    reInitCounter();
                }
                else
                {
                    ctn.start();
                }

            }


        }

    }

    public static int getRandomNumberBetween(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt(max - min) + min;
        if (randomNumber == min) {
            // Since the random number is between the min and max values, simply add 1
            return min + 1;
        } else {
            return randomNumber;
        }
    }

    private void reInitCounter()
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

              //  Toast.makeText(OnlineRunActivity.this, "Amjid", Toast.LENGTH_SHORT).show();
                ctn=   new CountDownTimer(30000, 1000) {

                    public void onTick(long duration) {
                        //tTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext resource id
                        // Duration
                      //  Toast.makeText(OnlineRunActivity.this, "Khan", Toast.LENGTH_SHORT).show();
                        isAutoTurnApplied=false;


                        Ssec = (duration / 1000) % 60;

                        if (isTurnApplied)
                        {
                       //     Toast.makeText(OnlineRunActivity.this, "Kamal", Toast.LENGTH_SHORT).show();
                            ctn.cancel();
                            timer.setText("");
                            isTurnApplied=false;
                            isConterStarted=false;
                         //   Toast.makeText(OnlineRunActivity.this, "Ssec"+Ssec, Toast.LENGTH_SHORT).show();


                        }
                        else if(Ssec < 31) {
                            timer.setText("" + Ssec+" secs");

                        }
                    }

                    public void onFinish() {
                        timer.setText("");

                     //   Toast.makeText(OnlineRunActivity.this, "Jamal"+Ssec, Toast.LENGTH_SHORT).show();

                        isConterStarted=false;
                        if (opponentUniqueId!=null && (!opponentUniqueId.equals("")))
                        {


                            int randPos=getRandomNumberBetween(1,10);
                            String pos=""+randPos;
                            boolean isNumOkay=false;
                            if (!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId))
                            {
                                isNumOkay=true;
                            }

                            while (!isNumOkay)
                            {
                                randPos=getRandomNumberBetween(1,10);
                                pos=""+randPos;
                                if (!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId))
                                {
                                    isNumOkay=true;
                                    break;
                                }

                            }

                            if (isNumOkay)
                            {
                                //Random Turn Here
                                if(!doneBoxes.contains(pos) && playerTurn.equals(playerUniqueId)){


                                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("box_position").setValue(pos);
                                    databaseReference.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size() + 1)).child("player_id").setValue(playerUniqueId);

                                    playerTurn = opponentUniqueId;
                                }
                                playerTurn = opponentUniqueId ;
                                isTurnApplied=false;
                                isAutoTurnApplied=true;
                                applyPlayerTurn(playerTurn);


                            }



                        }
                        if (isAutoTurnApplied)
                        {
                            reInitCounter();
                        }
                        else {
                            ctn.start();
                        }
                    }

                };

                ctn.start();
            }

        });
    }
}