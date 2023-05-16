package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rentalapplication.Adapter.DisplayRoomAdapter;
import com.example.rentalapplication.Adapter.RatingAndReviewAdapter;
import com.example.rentalapplication.model.RatingAndReviewModel;
import com.example.rentalapplication.model.addRoomDataHolder;
import com.example.rentalapplication.model.chatModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayReviewAndRating extends AppCompatActivity {

    ImageButton backbtn;

    RatingAndReviewAdapter adapter;
    androidx.appcompat.widget.Toolbar DisplayRatingAndReviewtoolbar;
    RecyclerView rcvRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_review_and_rating);

        backbtn= findViewById(R.id.reviewRatingbackImage);

        //to set the toolbar
        DisplayRatingAndReviewtoolbar=findViewById(R.id.reviewRatingtoolbar);
        setSupportActionBar(DisplayRatingAndReviewtoolbar);


        rcvRating=findViewById(R.id.rcvRating);
        rcvRating.setLayoutManager(new LinearLayoutManager(this));

        String random=getIntent().getStringExtra("randomnumber");



        FirebaseRecyclerOptions<RatingAndReviewModel> options =
                new FirebaseRecyclerOptions.Builder<RatingAndReviewModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rooms").child(random).child("ratingAndReview").limitToFirst(10), RatingAndReviewModel.class)
                        .build();



        adapter = new RatingAndReviewAdapter(options,getApplicationContext());
        rcvRating.setAdapter(adapter);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DisplayReviewAndRating.this,Displayrooms.class);
                startActivity(intent);

            }
        });

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