package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.model.ProfileDataHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageButton backImage;
    TextView viewProfileUserName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    //    Button updateProfile;
    FirebaseFirestore firebaseFirestore;
    ImageView viewUserImage;
    StorageReference storageReference;
    String ImageURIaccessToken;
    androidx.appcompat.widget.Toolbar profiletoolbar;

    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        backImage=findViewById(R.id.backImage);

        profiletoolbar=findViewById(R.id.profiletoolbar);
        viewUserImage = (ImageView) findViewById(R.id.viewUserImage);
        viewProfileUserName= (TextView) findViewById(R.id.viewProfileUserName);
//        updateProfile=(Button) findViewById(R.id.profileUpdate);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();

        Drawable toolbarDrawableProfile= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_more_vert_24);
        profiletoolbar.setOverflowIcon(toolbarDrawableProfile);

        setSupportActionBar(profiletoolbar);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),ChatLayoutActivity.class);
                startActivity(intent);

            }
        });

        storageReference=firebaseStorage.getReference("images");
        storageReference.child(firebaseAuth.getUid()).child("ProfileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccessToken=uri.toString();
                //display image with glide Library
//                Glide.with(getApplicationContext())
//                        .load(uri)
//                        .fitCenter()
//                        .into(viewUserImage);

//                display image with picasso library
                Picasso.get().load(uri).into(viewUserImage);
            }
        });

        //check this database reference in the profile data inserting page
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               ProfileDataHolder userProfile=snapshot.getValue(ProfileDataHolder.class);
                viewProfileUserName.setText(userProfile.getName());
//                ProfileDataHolder userProfile=snapshot.getValue(ProfileDataHolder.class);
//                viewProfileUserName.setText(userProfile.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to fetch datas",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, Displayrooms.class);
        startActivity(intent);

    }
}