package com.example.live_tik_tac_toe_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.live_tik_tac_toe_project.Adapters.userSpecificAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageToSpecific extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;

    RelativeLayout activity_main;

    private String rec_email="";
    private String sen_email="";
    //Add Emojicon
    EditText emojiconEditText;
    ImageView emojiButton,submitButton;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    private ArrayList<userSpecificModel> messagesFromDB;
    RecyclerView listOfMessage;
    Query query;
    FirebaseRecyclerOptions<ChatMessage> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_to_specific);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("UserChat");

        sen_email=mAuth.getCurrentUser().getUid().toString();
        rec_email=getIntent().getStringExtra("recEmail");


        // Toast.makeText(MessageToSpecific.this, rec_email, Toast.LENGTH_SHORT).show();

        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        listOfMessage = (RecyclerView) findViewById(R.id.list_of_message);
        listOfMessage.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance().getReference("UserChat");
        options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query,ChatMessage.class)
                .build();
        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=mAuth.getCurrentUser().getEmail().toString();
                //Toast.makeText(MessagesActivity.this, userEmail, Toast.LENGTH_SHORT).show();

                String messageId = mDatabase.push().getKey();
                String currentID = mAuth.getCurrentUser().getUid().toString();

                userSpecificModel userModel=new userSpecificModel(emojiconEditText.getText().toString(),
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


                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Add RecyclerView Code here
                messagesFromDB=new ArrayList<userSpecificModel>();
                for(DataSnapshot postSnapshots: dataSnapshot.getChildren())
                {
                    userSpecificModel userModel=postSnapshots.getValue(userSpecificModel.class);

                    String senderEmail=userModel.getSender();
                    String recieverEmail=userModel.getReciever();
                    if ((senderEmail.equals(sen_email)) || (recieverEmail.equals(sen_email)))
                    {
                        if (recieverEmail.equals(rec_email) || senderEmail.equals(rec_email))
                        {
                            messagesFromDB.add(userModel);
                        }



                    }




                }
                String userEmail=mAuth.getCurrentUser().getEmail();

                userSpecificAdapter adapter=new userSpecificAdapter(messagesFromDB,userEmail,MessageToSpecific.this);
                //MessageAdapter adapter=new MessageAdapter(messagesFromDB,userEmail,MessageToSpecific.this);
                listOfMessage.setLayoutManager(new LinearLayoutManager(MessageToSpecific.this));

                listOfMessage.setAdapter(adapter);
                listOfMessage.scrollToPosition(messagesFromDB.size()-1);
                adapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}