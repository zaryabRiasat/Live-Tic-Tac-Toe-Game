package com.example.live_tik_tac_toe_project.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.live_tik_tac_toe_project.Model.LeaderBoardModel;
import com.example.live_tik_tac_toe_project.R;
import com.example.live_tik_tac_toe_project.userSpecificModel;

import java.util.ArrayList;


public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    ArrayList<LeaderBoardModel> dataList;

    Context context;




    public LeaderBoardAdapter(ArrayList<LeaderBoardModel> dataList,  Context context) {

        this.dataList = dataList;

        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {




            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leader_board,parent,false);

            return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        LeaderBoardModel model=dataList.get(position);

        holder.tvRank1.setText(""+(position+1));
        holder.tvPlayerName1.setText(model.getName());
        holder.tvPoint1.setText(model.getScore());
    }


    public void setDataList(ArrayList<LeaderBoardModel> dataList) {
        this.dataList = dataList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRank1;
        TextView tvPlayerName1;

        TextView tvPoint1;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank1=itemView.findViewById(R.id.tvRank1);
            tvPlayerName1=itemView.findViewById(R.id.tvPlayerName1);

            tvPoint1=itemView.findViewById(R.id.tvPoint1);


        }
    }
}




