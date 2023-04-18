package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Displayrooms extends AppCompatActivity {

    RecyclerView rcvDisplayContainer;

    DisplayRoomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayrooms);

        rcvDisplayContainer = findViewById(R.id.rcvDisplayContainer);
        rcvDisplayContainer.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<addRoomDataHolder> options =
                new FirebaseRecyclerOptions.Builder<addRoomDataHolder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms"), addRoomDataHolder.class)
                        .build();
        adapter = new DisplayRoomAdapter(options,getApplicationContext());
        rcvDisplayContainer.setAdapter(adapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}