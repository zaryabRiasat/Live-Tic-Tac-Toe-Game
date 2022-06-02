package com.example.live_tik_tac_toe_project.Adapters;


import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.live_tik_tac_toe_project.R;
import com.example.live_tik_tac_toe_project.userSpecificModel;


import java.util.ArrayList;

public class userSpecificAdapter extends RecyclerView.Adapter<userSpecificAdapter.ViewHolder> {

    ArrayList<userSpecificModel> chatMessages;
    private String userEmail;
    Context context;
    private static int VIEW_SENDER=1;
    private static int VIEW_REC=2;




    public userSpecificAdapter(ArrayList<userSpecificModel> chatMessages, String userEmail, Context context) {

        this.chatMessages = chatMessages;
        this.userEmail=userEmail;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        if (viewType==VIEW_SENDER)
        {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send_row,parent,false);

            return new ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recieve_row,parent,false);

            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.txt_email.setText(chatMessages.get(position).getMessageUser());
        holder.txt_message.setText(chatMessages.get(position).getMessageText());
        holder.txt_time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",chatMessages.get(position).getMessageTime()));

    }

    @Override
    public int getItemViewType(int position) {
        String Email=chatMessages.get(position).getMessageUser();

        if (Email.equals(userEmail))
        {
            return VIEW_SENDER;
        }
        else
        {
            return VIEW_REC;
        }




    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_email;
        TextView txt_message;

        TextView txt_time;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_email=itemView.findViewById(R.id.txt_email);
            txt_message=itemView.findViewById(R.id.txt_mesg);

            txt_time=itemView.findViewById(R.id.txt_time);


        }
    }
}



