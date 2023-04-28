package com.example.rentalapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserProfile extends AppCompatActivity {

    String randomKey, name1;
    StorageReference storageReference;
    Button signupProfile;
    EditText name;
    ImageView img;
    Uri filePath;
    Bitmap bmap;
    ProfileDataHolder holder;

    FirebaseAuth firebaseAuth;
    private String StrName, ImageUriAccessToken;

    FirebaseFirestore firebasefirestore;

    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        img = (ImageView) findViewById(R.id.userProfilePicture);
        signupProfile = (Button) findViewById(R.id.saveProfileData);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebasefirestore = FirebaseFirestore.getInstance();


        img = (ImageView) findViewById(R.id.userProfilePicture);
        signupProfile = (Button) findViewById(R.id.saveProfileData);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose the image"), 1);
            }
        });

        signupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

                bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bmap);

            } catch (Exception e) {
                Toast.makeText(this, "exception in image part", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "issue in data or something", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadToFirebase() {

        name = findViewById(R.id.userProfileName);
        img = findViewById(R.id.userProfilePicture);

        name1 = name.getText().toString().trim();

        if (name1.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter your name", Toast.LENGTH_LONG).show();
        } else if (filePath == null) {
            Toast.makeText(getApplicationContext(), "Please Upload your Image", Toast.LENGTH_LONG).show();
        } else {


            FirebaseDatabase Fdata = FirebaseDatabase.getInstance();
//            DatabaseReference ref = Fdata.getReference().child("users").child(firebaseAuth.getUid());
            DatabaseReference ref = Fdata.getReference(firebaseAuth.getUid());

            ProfileDataHolder profileDataHolder = new ProfileDataHolder(name1, firebaseAuth.getUid());
            ref.setValue(profileDataHolder);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference("images").child(firebaseAuth.getUid()).child("ProfileImage");

            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.JPEG, 20, byteOutputStream);
            byte[] data = byteOutputStream.toByteArray();

//            storageReference.putFile(filePath)
            storageReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ImageUriAccessToken = uri.toString();
                            Toast.makeText(UserProfile.this, "Uri get Successfully", Toast.LENGTH_SHORT).show();

                            storetofirestore();


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
                    Toast.makeText(UserProfile.this, "FAILED TO UPLOAD IMAGE", Toast.LENGTH_LONG).show();
                }
            });
            Toast.makeText(this, "Image Uploaded.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(UserProfile.this, ChatLayoutActivity.class);
        startActivity(intent);
        finish();
    }

    private void storetofirestore() {

        DocumentReference dReference = firebasefirestore.collection("Users").document(firebaseAuth.getUid());

        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name1);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "online");
        Toast.makeText(this, "Hi here", Toast.LENGTH_SHORT).show();
        dReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserProfile.this, "Data on clould firestore", Toast.LENGTH_SHORT).show();
                name.setText("");
                img.setImageResource(R.drawable.ic_launcher_background);

            }
        });


    }


}