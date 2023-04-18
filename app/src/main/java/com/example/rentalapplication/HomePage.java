package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class HomePage extends AppCompatActivity {

    RadioGroup RoomOrFlat;
    RadioButton rooms,flat;
    LinearLayout RoomsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        RoomOrFlat= findViewById(R.id.RoomOrFlat);

        rooms = findViewById(R.id.Room);
        flat = findViewById(R.id.Flat);
        RoomsLayout = findViewById(R.id.RoomsLayout);
    }
    public void ShowRooms(View v){
        if(rooms.isChecked()){
            RoomsLayout.setVisibility(View.VISIBLE);
        } else if (flat.isChecked()) {
            RoomsLayout.setVisibility(View.GONE);
        }
    }
}