package com.example.rentalapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalapplication.R;
import com.example.rentalapplication.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class IndividualMessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageArrayLists;
    int ITEM_SENT=1;
    int ITEM_RECEIVED=2;

    public IndividualMessagesAdapter(Context context, ArrayList<Message> messageArrayLists) {
        this.context = context;
        this.messageArrayLists = messageArrayLists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==ITEM_SENT){
            View view= LayoutInflater.from(context).inflate(R.layout.sendchatlayout,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.receivechatlayout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=messageArrayLists.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder senderViewHolder=(SenderViewHolder) holder;
            senderViewHolder.textViewMessage.setText(message.getMessage());
            senderViewHolder.timeofMessage.setText(message.getCurrentTime());
        }else{
            ReceiverViewHolder receiverViewHolder=(ReceiverViewHolder) holder;
            receiverViewHolder.ReceivedtextViewMessage.setText(message.getMessage());
            receiverViewHolder.ReceivedtimeofMessage.setText(message.getCurrentTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message messageModel= messageArrayLists.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel.getSenderId()))
        {
            return ITEM_SENT;
        }
        else {
            return ITEM_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayLists.size();
    }
    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage,timeofMessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage=itemView.findViewById(R.id.senderMessage);
            timeofMessage=itemView.findViewById(R.id.sendertimeOfMessage);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView ReceivedtextViewMessage,ReceivedtimeofMessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            ReceivedtextViewMessage=itemView.findViewById(R.id.receivedMessage);
            ReceivedtimeofMessage=itemView.findViewById(R.id.receivertimeOfMessage);
        }
    }
}
