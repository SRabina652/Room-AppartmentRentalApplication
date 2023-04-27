package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms").limitToFirst(10), addRoomDataHolder.class)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_room,menu);
        MenuItem item= menu.findItem(R.id.searchbar);

        SearchView search=(SearchView) item.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRoom(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchRoom(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchRoom(String query) {
        FirebaseRecyclerOptions<addRoomDataHolder> options =
                new FirebaseRecyclerOptions.Builder<addRoomDataHolder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms").orderByChild("search").startAt(query).endAt(query+ "\uf8ff"), addRoomDataHolder.class)
                        .build();
        adapter = new DisplayRoomAdapter(options,getApplicationContext());
        adapter.startListening();
        rcvDisplayContainer.setAdapter(adapter);

    }
}