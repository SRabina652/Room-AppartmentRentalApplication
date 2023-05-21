package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.Adapter.IndividualMessagesAdapter;
import com.example.rentalapplication.Fragment.ChatFragment;
import com.example.rentalapplication.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class individualChat extends AppCompatActivity {

    TextInputEditText IndividualChatMessage;   //mgetmessage
    ImageButton messageSendButton;   //msendmessagebutton
    CardView messageSentCardView;  //mnameofspecifiecuser

    androidx.appcompat.widget.Toolbar individualchatToolbar; // mtoolbarofspecificchat
    TextView nameOfSpecificUser; //nmaneofspecificuser

    String sendingMessage; //enteredMessage
    Intent intent;

    String receivername,receiveruid,senderuid;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
    String senderRoom,receiverRoom;

    ImageView individualProfileImage;
    ImageButton SendMessagebuttonIndividualChat,IndividualChatbackImage;

    RecyclerView rcvView;  //mmessagerecyclerview


    IndividualMessagesAdapter individualMessagesAdapter;
    ArrayList<Message> messageModelArrayList;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat);

        IndividualChatMessage=findViewById(R.id.IndividualChatMessage);
        messageSentCardView=findViewById(R.id.messageSendCartView);
        SendMessagebuttonIndividualChat=findViewById(R.id.sendMessageImageView);
        individualchatToolbar=findViewById(R.id.IndividualChattoolbar);
        nameOfSpecificUser=findViewById(R.id.IndividualChattoolbarName);

        IndividualChatbackImage=findViewById(R.id.IndividualChatbackImage);
        IndividualChatbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(individualChat.this, ChatFragment.class);
                startActivity(intent1);
            }
        });

        messageModelArrayList=new ArrayList<>();
        rcvView=findViewById(R.id.IndividualRcvView);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        rcvView.setLayoutManager(linearLayoutManager);
        individualMessagesAdapter= new IndividualMessagesAdapter(individualChat.this,messageModelArrayList);
        rcvView.setAdapter(individualMessagesAdapter);


        intent =getIntent();
        setSupportActionBar(individualchatToolbar);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        senderuid=firebaseAuth.getUid();
        receiveruid=getIntent().getStringExtra("receiverUid");
        receivername=getIntent().getStringExtra("name");
        senderRoom=senderuid + receiveruid;
        receiverRoom = receiveruid +senderuid;

//        JUST TO CHECK WEATHER THE RESEIVER uid are received or not
//        Toast.makeText(this,"Receiver Uid"+ receiveruid, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this,"sender Uid"+ senderuid, Toast.LENGTH_SHORT).show();

//
        individualProfileImage=findViewById(R.id.individualProfileImage);





        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats")
                .child(senderRoom).child("messages");
        individualMessagesAdapter= new IndividualMessagesAdapter(individualChat.this,messageModelArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelArrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Message messageModel= snapshot1.getValue(Message.class);
                    messageModelArrayList.add(messageModel);
                }
                individualMessagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        nameOfSpecificUser.setText(receivername);

        if(intent.getStringExtra("imageUri")==null){

        }else{
            String filepath=intent.getStringExtra("imageUri");
            Picasso.get().load(filepath).into(individualProfileImage);

        }

//        if(filepath.isEmpty()){
//            Toast.makeText(this, "No image is Displayed", Toast.LENGTH_SHORT).show();
//        }else{
//            Picasso.get().load(filepath).into(individualProfileImage);
//        }



        //checks weather the message was sent or not

        SendMessagebuttonIndividualChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingMessage=IndividualChatMessage.getText().toString();
                if(sendingMessage.isEmpty()){
                    Toast.makeText(individualChat.this, "You Cannot send null Message", Toast.LENGTH_SHORT).show();
                }else {
                    Date date= new Date();
                    currentTime=simpleDateFormat.format(calendar.getTime());
                    Message messageModel= new Message(sendingMessage,firebaseAuth.getUid(), date.getTime(), currentTime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom).child("messages").push().setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference().child("chats")
                                            .child(receiverRoom)
                                            .child("messages")
                                            .push().setValue(messageModel)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(individualChat.this, "Message sent", Toast.LENGTH_SHORT).show();
                                                    IndividualChatMessage.setText("");
                                                }
                                            });
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        individualMessagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(individualMessagesAdapter!=null){
            individualMessagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, ChatLayoutActivity.class);
        startActivity(intent);

    }
}