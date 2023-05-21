package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rentalapplication.model.ProfileDataHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingleRoomDisplayActivity extends AppCompatActivity {

    String longitude;
    String latitude;

    String ownersuid;

    Button chatBtn;

    ImageView imgdisplay;
    TextView people,price,landmark,requirements,facilities,rooms,typetoUseRooms;

    TextView giveReviewRating,viewRatingAndReview;

    TextView getMap;

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

        getMap=findViewById(R.id.getMap);

        Glide.with(imgdisplay.getContext()).load(getIntent().getStringExtra("images")).into(imgdisplay);
        price.setText(getIntent().getStringExtra("price"));
        people.setText(getIntent().getStringExtra("people"));
        landmark.setText(getIntent().getStringExtra("landmark"));
        rooms.setText(getIntent().getStringExtra("rooms"));
        requirements.setText(getIntent().getStringExtra("requirements"));
        facilities.setText(getIntent().getStringExtra("facilities"));
        typetoUseRooms.setText(getIntent().getStringExtra("typeOfAppliers"));

        longitude = getIntent().getStringExtra("longitude");
        latitude = getIntent().getStringExtra("latitude");

        ownersuid=getIntent().getStringExtra("ownersuid");


         if (ownersuid==null){
             Toast.makeText(this, "This is null", Toast.LENGTH_SHORT).show();
         }else {
             Toast.makeText(this, "This is not null", Toast.LENGTH_SHORT).show();
         }

        giveReviewRating=findViewById(R.id.giveReviewRating);

        giveReviewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SingleRoomDisplayActivity.this,RatingAndReview.class);
                intent.putExtra("randomNumber",getIntent().getStringExtra("RandomNumber"));
                startActivity(intent);
            }
        });
        chatBtn=findViewById(R.id.chats);



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference=firebaseDatabase.getReference(ownersuid);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ProfileDataHolder userProfile=snapshot.getValue(ProfileDataHolder.class);

                        Intent intent = new Intent(SingleRoomDisplayActivity.this,individualChat.class);
                        intent.putExtra("name",userProfile.getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Failed to fetch datas",Toast.LENGTH_LONG).show();
                    }
                });


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

        getMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri geoUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=15&q=" + latitude + "," + longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
                intent.setPackage("com.google.android.apps.maps");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(SingleRoomDisplayActivity.this, "Error While Opening Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, Displayrooms.class);
        startActivity(intent);

    }
}