package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.model.RatingAndReviewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingAndReview extends AppCompatActivity {
    RatingBar userRatingBar;
    Float ratedNumber;

    EditText userReview;

    String rootnode, name;
    TextView submitReviewAndRating;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rating_and_review);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

                DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child(uniqueUserId);

                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name=snapshot.child("name").getValue().toString();

                        if((name.isEmpty() || ratedNumber==0) || (name.isEmpty()&& ratedNumber==0)){
                            Toast.makeText(RatingAndReview.this, "You cannot submit null rating and review", Toast.LENGTH_SHORT).show();
                        }else{
                            RatingAndReviewModel rrModel=new RatingAndReviewModel(ratedNumber,userReview.getText().toString().trim(),uniqueUserId,name);
                            dataReference.child(rootnode).child("ratingAndReview").child(uniqueUserId).setValue(rrModel);
                            Intent intent=new Intent(RatingAndReview.this,DisplayReviewAndRating.class);
                            intent.putExtra("randomnumber",rootnode);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, Displayrooms.class);
        startActivity(intent);

    }
}