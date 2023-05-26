package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    ImageButton profile, logout;
    TextView viewRooms, PostRooms,chatwithOthers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        profile=findViewById(R.id.profilebutton);
        logout=findViewById(R.id.logoutbutton);

        viewRooms=findViewById(R.id.viewRooms);

        PostRooms=findViewById(R.id.PostRooms);

        chatwithOthers=findViewById(R.id.chatwithOthers);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //activity to view Profile
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomePage.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        //button event listener to logout the app
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent intent = new Intent(HomePage.this, Signup.class);
                startActivity(intent);
                finish();
            }
        });

        //event Listener to view all the rooms
        viewRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomePage.this,Displayrooms.class);
                startActivity(intent);
            }
        });


        // Add Rooms
        PostRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomePage.this,SelectLocation.class);
                startActivity(intent);
            }
        });

        //redirecting to the chat activity

        chatwithOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomePage.this,ChatLayoutActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }
//    public void ShowRooms(View v){
//        if(rooms.isChecked()){
//            RoomsLayout.setVisibility(View.VISIBLE);
//        } else if (flat.isChecked()) {
//            RoomsLayout.setVisibility(View.GONE);
//        }
//    }
}