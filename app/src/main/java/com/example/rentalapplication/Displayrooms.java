package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rentalapplication.Adapter.DisplayRoomAdapter;
import com.example.rentalapplication.Adapter.RoomDisplayAdapter;
import com.example.rentalapplication.model.addRoomDataHolder;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Displayrooms extends AppCompatActivity {

    RecyclerView rcvDisplayContainer;

//    DisplayRoomAdapter adapter;
    RoomDisplayAdapter roomAdapter;

    SearchView searchView;

//    androidx.appcompat.widget.Toolbar DisplayRoomstoolbar;

    private DatabaseReference databaseReference;

    private ArrayList<addRoomDataHolder> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayrooms);

        rcvDisplayContainer = findViewById(R.id.rcvDisplayContainer);
//        rcvDisplayContainer.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms");
        rcvDisplayContainer.setLayoutManager(new LinearLayoutManager(this));

//        dataList = new ArrayList<>();
//        roomAdapter = new RoomDisplayAdapter(dataList,context);
//        rcvDisplayContainer.setAdapter(roomAdapter);
//        DisplayRoomstoolbar=findViewById(R.id.displayRoomtoolbar);
//        setSupportActionBar(DisplayRoomstoolbar);


//        FirebaseRecyclerOptions<addRoomDataHolder> options =
//                new FirebaseRecyclerOptions.Builder<addRoomDataHolder>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms").limitToFirst(10), addRoomDataHolder.class)
//                        .build();
//        adapter = new DisplayRoomAdapter(options,getApplicationContext());
//        rcvDisplayContainer.setAdapter(adapter);


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
                        RoomDisplayAdapter roomDisplayAdapter = new RoomDisplayAdapter(dataList, getApplicationContext());
                        rcvDisplayContainer.setAdapter(roomDisplayAdapter);
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

//        adapter.startListening();
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        roomAdapter.stopListening();
//    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_room,menu);
//        MenuItem item= menu.findItem(R.id.searchbar);
//
//        SearchView search=(SearchView) item.getActionView();
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchRoom(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                searchRoom(query);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
    private void searchRoomData(String s) {

        ArrayList<addRoomDataHolder> myList= new ArrayList<>();
        for(addRoomDataHolder object:dataList){
            if(object.getSearch().toLowerCase().contains(s.toLowerCase())){
                myList.add(object);
            }
        }

        RoomDisplayAdapter roomDisplayAdapter = new RoomDisplayAdapter(myList, getApplicationContext());
        rcvDisplayContainer.setAdapter(roomDisplayAdapter);
    }

//    private void searchRoom(String query) {
//        FirebaseRecyclerOptions<addRoomDataHolder> options =
//                new FirebaseRecyclerOptions.Builder<addRoomDataHolder>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms").orderByChild("search").startAt(query).endAt(query + "\uf8ff"), addRoomDataHolder.class)
//                        .build();
//        adapter = new DisplayRoomAdapter(options, getApplicationContext());
//        adapter.startListening();
//        rcvDisplayContainer.setAdapter(adapter);
//
//    }
}