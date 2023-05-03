package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.rentalapplication.model.RatingAndReviewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingAndReview extends AppCompatActivity {
    RatingBar userRatingBar;
    Float ratedNumber;

    EditText userReview;

    String rootnode, name;
    Button submitReviewAndRating;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_and_review);
        userRatingBar= (RatingBar) findViewById(R.id.UserRatingStars);
        userReview=(EditText)findViewById(R.id.UserReview);

        submitReviewAndRating=findViewById(R.id.submitReviewAndRating);


        submitReviewAndRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratedNumber = userRatingBar.getRating();
                rootnode=getIntent().getStringExtra("randomNumber");


                FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
                DatabaseReference dataReference = FBdatabase.getReference("Rooms");
                firebaseAuth=FirebaseAuth.getInstance();

                String uniqueUserId=firebaseAuth.getUid();
//                Toast.makeText(RatingAndReview.this, uniqueUserId, Toast.LENGTH_SHORT).show();

                RatingAndReviewModel rrModel=new RatingAndReviewModel(ratedNumber,userReview.getText().toString().trim());

                dataReference.child(rootnode).child("ratingAndReview").child(uniqueUserId).setValue(rrModel);

                Intent intent=new Intent(RatingAndReview.this,DisplayReviewAndRating.class);
                startActivity(intent);
            }
        });

    }
}