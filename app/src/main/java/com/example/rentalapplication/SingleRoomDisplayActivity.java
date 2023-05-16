package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SingleRoomDisplayActivity extends AppCompatActivity {

    ImageView imgdisplay;
    TextView people,price,landmark,requirements,facilities,rooms,typetoUseRooms;

    Button giveReviewRating,viewRatingAndReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room_display);

        imgdisplay = findViewById(R.id.imagedisplay);
        price = findViewById(R.id.displaypriceDesc);
        people = findViewById(R.id.displayPeopleDesc);
        landmark = findViewById(R.id.LandmarkDetails);
        requirements= findViewById(R.id.requirementsDetails);
        facilities=findViewById(R.id.FacilitiesDetails);
        rooms = findViewById(R.id.RoomsDetails);
        typetoUseRooms = findViewById(R.id.typetoUseRooms);

        viewRatingAndReview=findViewById(R.id.viewRatingandreview);

        Glide.with(imgdisplay.getContext()).load(getIntent().getStringExtra("images")).into(imgdisplay);
        price.setText(getIntent().getStringExtra("price"));
        people.setText(getIntent().getStringExtra("people"));
        landmark.setText(getIntent().getStringExtra("landmark"));
        rooms.setText(getIntent().getStringExtra("rooms"));
        requirements.setText(getIntent().getStringExtra("requirements"));
        facilities.setText(getIntent().getStringExtra("facilities"));
        typetoUseRooms.setText(getIntent().getStringExtra("typeOfAppliers"));

        giveReviewRating=findViewById(R.id.giveReviewRating);

        giveReviewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SingleRoomDisplayActivity.this,RatingAndReview.class);
                intent.putExtra("randomNumber",getIntent().getStringExtra("RandomNumber"));
                startActivity(intent);
            }
        });

        viewRatingAndReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SingleRoomDisplayActivity.this,DisplayReviewAndRating.class);
                intent.putExtra("randomnumber",getIntent().getStringExtra("RandomNumber"));
                startActivity(intent);
            }
        });

    }
}