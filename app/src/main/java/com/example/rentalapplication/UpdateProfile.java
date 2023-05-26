package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.model.ProfileDataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    String updatename;
    StorageReference storageReference;
    TextView UpdateProfiledata;
    EditText updateUsername;
    ImageView updateimg;
    Uri filePath;
    Bitmap bmap;
    ProfileDataHolder holder;

    FirebaseAuth firebaseAuth;
    private String StrUpdateName, ImageUriAccessToken;

    FirebaseFirestore firebasefirestore;
    FirebaseDatabase firebaseDatabase;

    private FirebaseStorage firebaseStorage;
    Intent intent;
    Toolbar updatetoolbar;
    ImageButton UpdateProfilebackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        updatetoolbar = findViewById(R.id.IndividualUpdateProfiletoolbar);
        UpdateProfilebackImage = findViewById(R.id.IndividualUpdateProfilebackImage);
        updateimg = findViewById(R.id.userUpdateProfilePicture);
        updateUsername = findViewById(R.id.userUpdateProfileName);
        UpdateProfiledata = findViewById(R.id.saveUpdateProfileData);


        setSupportActionBar(updatetoolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        firebasefirestore = FirebaseFirestore.getInstance();

        intent = getIntent();


        updateUsername.setText(intent.getStringExtra("name"));

        UpdateProfilebackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfile.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        UpdateProfiledata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatename = updateUsername.getText().toString();

                if (updatename.isEmpty()) {
                    Toast.makeText(UpdateProfile.this, "You have to enter your name", Toast.LENGTH_SHORT).show();
                } else if (filePath != null) {
                    ProfileDataHolder profiledataHolder = new ProfileDataHolder(updatename, firebaseAuth.getUid());
                    databaseReference.setValue(profiledataHolder);
                    updateImageDatatoStorage();

                    Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(UpdateProfile.this, Displayrooms.class);
                    startActivity(intent1);
                    finish();
                } else {
                    ProfileDataHolder profiledataHolder = new ProfileDataHolder(updatename, firebaseAuth.getUid());
                    databaseReference.setValue(profiledataHolder);

                    updateNameDatatoStorage();

                    Toast.makeText(UpdateProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(UpdateProfile.this, Displayrooms.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });


        updateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose the image"), 1);
            }
        });


        storageReference = firebaseStorage.getReference("images");
        storageReference.child(firebaseAuth.getUid()).child("ProfileImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageUriAccessToken = uri.toString();
                Picasso.get().load(uri).into(updateimg);
            }
        });
    }

    private void updateNameDatatoStorage() {
        DocumentReference dReference = firebasefirestore.collection("Users").document(firebaseAuth.getUid());

        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", updatename);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "online");


        dReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateProfile.this, "Updated Data on clould firestore", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateImageDatatoStorage() {

        updatename = updateUsername.getText().toString().trim();

            FirebaseDatabase Fdata = FirebaseDatabase.getInstance();
            DatabaseReference ref = Fdata.getReference(firebaseAuth.getUid());

            ProfileDataHolder profileDataHolder = new ProfileDataHolder(updatename, firebaseAuth.getUid());
            ref.setValue(profileDataHolder);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference("images").child(firebaseAuth.getUid()).child("ProfileImage");

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.JPEG, 20, byteOutputStream);
            byte[] data = byteOutputStream.toByteArray();

            storageReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageUriAccessToken = uri.toString();
                            updateNameDatatoStorage();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

//                    Toast.makeText(getApplicationContext(),"Image Uploading....",Toast.LENGTH_LONG).show();
//                    float progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                    dialogue.setMessage("Uploaded: " + (int) progress + "%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateProfile.this, "FAILED TO Update IMAGE", Toast.LENGTH_LONG).show();
                }
            });
            Toast.makeText(this, "Image Updated.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

                bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                updateimg.setImageBitmap(bmap);

            } catch (Exception e) {
                Toast.makeText(this, "exception in image part", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "issue in data or something", Toast.LENGTH_LONG).show();
        }
    }
}