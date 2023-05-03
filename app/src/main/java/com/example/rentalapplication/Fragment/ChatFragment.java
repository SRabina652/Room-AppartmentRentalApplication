package com.example.rentalapplication.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.R;
import com.example.rentalapplication.individualChat;
import com.example.rentalapplication.model.chatModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment {

    RecyclerView rcvChat;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ImageView imageViewHolder;
    LinearLayoutManager linearLayoutManager;
    FirestoreRecyclerAdapter<chatModel,ChatViewHolder> chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        rcvChat = view.findViewById(R.id.rcvChat);


        Query query= firebaseFirestore.collection("Users").whereNotEqualTo("uid",firebaseAuth.getUid());
//        Query query= firebaseFirestore.collection("Users");

        FirestoreRecyclerOptions<chatModel> everyUserName=new FirestoreRecyclerOptions.Builder<chatModel>().setQuery(query,chatModel.class).build();


        chatAdapter= new FirestoreRecyclerAdapter<chatModel, ChatViewHolder>(everyUserName) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull chatModel chatModel) {
                holder.usersHolderName.setText(chatModel.getName());
                String imagepath=chatModel.getImage();

                Picasso.get().load(imagepath).into(imageViewHolder);

                if(chatModel.getStatus().equals("online")){
                    holder.userStatusData.setText(chatModel.getStatus());
                    holder.userStatusData.setTextColor(Color.GREEN);
                }else {
                    holder.userStatusData.setText(chatModel.getStatus());
                }



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(getActivity(), individualChat.class);
                        intent.putExtra("name",chatModel.getName());
                        intent.putExtra("receiverUid",chatModel.getUid());
                        intent.putExtra("imageUri",chatModel.getImage());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View holderView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chatview_layout,parent,false);
                return new ChatViewHolder(holderView);
            }
        };

        rcvChat.setHasFixedSize(true);
        linearLayoutManager= new LinearLayoutManager(getContext());
        rcvChat.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rcvChat.setAdapter(chatAdapter);

        return view;
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder{


        TextView usersHolderName,userStatusData;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            usersHolderName=itemView.findViewById(R.id.chatUserName);
            imageViewHolder=itemView.findViewById(R.id.userChatImage);
            userStatusData=itemView.findViewById(R.id.userStatusData);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null){
            chatAdapter.stopListening();
        }
    }
}