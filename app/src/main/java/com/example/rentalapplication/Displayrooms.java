package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.Adapter.DisplayRoomAdapter;
import com.example.rentalapplication.Adapter.RoomDisplayAdapter;
import com.example.rentalapplication.model.addRoomDataHolder;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Displayrooms extends AppCompatActivity {

    RecyclerView rcvDisplayContainer;

    RoomDisplayAdapter roomAdapter;

    String Customersuid;
    SearchView searchView;

    private DatabaseReference databaseReference;

    private ArrayList<addRoomDataHolder> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayrooms);

        rcvDisplayContainer = findViewById(R.id.rcvDisplayContainer);
        searchView = findViewById(R.id.searchView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms");
        rcvDisplayContainer.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            Customersuid = user.getUid();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        dataList = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            dataList.add(ds.getValue(addRoomDataHolder.class));
                        }
                        RoomDisplayAdapter roomDisplayAdapter = new RoomDisplayAdapter(dataList, getApplicationContext(),Customersuid);
                        rcvDisplayContainer.setAdapter(roomDisplayAdapter);

                        //this is to delete the row
                        roomDisplayAdapter.setOnItemClickListener(new DisplayRoomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                dataList.remove(position);
                                roomDisplayAdapter.notifyItemRemoved(position);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    searchRoomData(s);
                    return true;
                }
            });
        }

    }


    private void searchRoomData(String s) {

        ArrayList<addRoomDataHolder> myList= new ArrayList<>();
        for(addRoomDataHolder object:dataList){
            if(object.getSearch().toLowerCase().contains(s.toLowerCase())){
                myList.add(object);
            }
        }



        RoomDisplayAdapter roomDisplayAdapter = new RoomDisplayAdapter(myList, getApplicationContext(),Customersuid);
        rcvDisplayContainer.setAdapter(roomDisplayAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

    }
}