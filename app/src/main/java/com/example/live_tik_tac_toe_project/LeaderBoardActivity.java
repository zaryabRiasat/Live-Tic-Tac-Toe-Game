package com.example.live_tik_tac_toe_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.WorkSource;
import android.widget.Toast;

import com.example.live_tik_tac_toe_project.Adapters.LeaderBoardAdapter;
import com.example.live_tik_tac_toe_project.Model.LeaderBoardModel;
import com.example.live_tik_tac_toe_project.Model.WonClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderBoardActivity extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase;
     ArrayList<String> playerNames;
     ArrayList<WonClass> playerList;
     ArrayList<Integer> totals;
     ArrayList<LeaderBoardModel> rankList;

     private RecyclerView recycler;

     private LeaderBoardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        recycler=findViewById(R.id.recycler);

        rankList=new ArrayList<>();

        mAdapter=new LeaderBoardAdapter(rankList,LeaderBoardActivity.this);
        recycler.setLayoutManager(new LinearLayoutManager(LeaderBoardActivity.this));
        recycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
        playerNames=new ArrayList<>();
        playerList=new ArrayList<>();
        totals=new ArrayList<>();
        rankList=new ArrayList<>();


        FirebaseDatabase.getInstance().getReference("won").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    WonClass wonClass=snapshot1.getValue(WonClass.class);
                    if (wonClass!=null && (!playerNames.contains(wonClass.getPlayer_name())))
                    {
                        playerNames.add(wonClass.getPlayer_name());

                    }

                    playerList.add(wonClass);

//                    Toast.makeText(LeaderBoardActivity.this, "data "+wonClass.getPlayer_id(), Toast.LENGTH_SHORT).show();
                }
                sortList();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sortList()
    {
        for (int i=0;i<playerNames.size();i++)
        {

            int total=0;
            for (int j=0;j<playerList.size();j++)
            {
                WonClass model=playerList.get(j);

                if (model.getPlayer_name().equalsIgnoreCase(playerNames.get(i)))
                {
                    total+=model.getScore();
                }
            }

            totals.add(total);

        }



        //Sorting in Descending order
            int n = totals.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++)
                    if (totals.get(j) < totals.get(j + 1)) {
                        // swap arr[j+1] and arr[j]
                        int temp = totals.get(j);
                        totals.set(j, totals.get(j + 1));
                        totals.set((j + 1), temp);


                        String tempNm = playerNames.get(j);

                        playerNames.set(j, playerNames.get(j + 1));
                        playerNames.set((j + 1), tempNm);
                    }

            }
        rankList=new ArrayList<>();
            for (int i=0;i<playerNames.size();i++)
            {
                LeaderBoardModel model=new LeaderBoardModel();
                model.setName(playerNames.get(i));
                model.setScore(""+totals.get(i));
                rankList.add(model);

            }

            mAdapter.setDataList(rankList);
    }
}