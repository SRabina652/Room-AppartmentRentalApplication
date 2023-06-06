package com.example.rentalapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentalapplication.model.addRoomDataHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class AddRooms extends AppCompatActivity {
    static Integer node = 0;

    String checkedItems = "";
    String search="";
    String getPrice="";
    String getPeople="";
    String getRequirement="";
    String getlandmark="";
    String getFacilities="";
    CheckBox checkbox_Student, checkbox_Family, checkbox_women, checkbox_men;
    TextInputEditText price, peoples, requirement, facilities, Rooms;
    EditText landmark;
    ImageView roomImg;
    Uri fPath;
    TextView add;
    Bitmap bmap;
    //to generate unique value
    String nodeval;

    String ownersuid;
    private FirebaseAuth mAuth;
    String longitude;
    String latitude;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String tag = "PERMISSION_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //checkboxs

        checkbox_Student = findViewById(R.id.checkbox_Student);
        checkbox_Family = findViewById(R.id.checkbox_Family);
        checkbox_women = findViewById(R.id.checkbox_women);
        checkbox_men = findViewById(R.id.checkbox_men);


        Rooms = findViewById(R.id.Rooms);


        roomImg = findViewById(R.id.roomImg);

        price = findViewById(R.id.price);
        peoples = findViewById(R.id.peoples);
        requirement = findViewById(R.id.requirement);
        facilities = findViewById(R.id.facilities);

        landmark = findViewById(R.id.plandmark);

        add = findViewById(R.id.roomsubmit);

        //get Location Longitude and Latitude from the select map section

        Intent intent = getIntent();

        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");


        mAuth = FirebaseAuth.getInstance();

        // Generate a UID for the current user

        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
        ownersuid = firebaseAuth.getUid();



        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            fPath = data.getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(fPath);
                                bmap = BitmapFactory.decodeStream(inputStream);
                                roomImg.setImageBitmap(bmap);
                            } catch (Exception e) {

                            }
                        }
                    }
                });

        roomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            fPath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(fPath);
                bmap = BitmapFactory.decodeStream(inputStream);
                roomImg.setImageBitmap(bmap);
            } catch (Exception e) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadDataToFirebase() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Data Uploading");
        dialog.show();

        FirebaseStorage dataStorage = FirebaseStorage.getInstance();
        StorageReference dataUploader = dataStorage.getReference("roomImage" + new Random().nextInt(50));

        String student = "Student";
        String family = "Family";
        String women = "Women";
        String men = "Men";


        if (checkbox_Student.isChecked()) {
            checkedItems += student;
        }
        if (checkbox_Family.isChecked()) {
            checkedItems = checkedItems + ", " + family;
        }
        if (checkbox_women.isChecked()) {
            checkedItems = checkedItems + ", " + women;
        }
        if (checkbox_men.isChecked()) {
            checkedItems = checkedItems + ", " + men;
        }

        if(fPath==null){
            Toast.makeText(this, "Please Upload Image", Toast.LENGTH_SHORT).show();
        }else{
            dataUploader.putFile(fPath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            dataUploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference dataReference = FBdatabase.getReference("Rooms");
                                    getPrice=price.getText().toString().trim();
                                    getPeople=peoples.getText().toString().trim();
                                    getRequirement=requirement.getText().toString().trim();
                                    getlandmark=landmark.getText().toString().trim();
                                    getFacilities=facilities.getText().toString().trim();
                                    search = Rooms.getText().toString().trim() + " " + checkedItems + " " + getPrice +
                                            " " + getPeople + " " + getFacilities +" "+getRequirement +" " + getlandmark;
                                    nodeval = UUID.randomUUID().toString().replaceAll("-", "");

                                    if(getPrice.isEmpty() || getPeople.isEmpty() || getRequirement.isEmpty() || getlandmark.isEmpty() || getFacilities.isEmpty() || fPath.getPath() == null){
                                        Toast.makeText(AddRooms.this, "Please give all the details in the form", Toast.LENGTH_SHORT).show();
                                    }else {
                                        addRoomDataHolder holder = new addRoomDataHolder("false", nodeval, search, Rooms.getText().toString().trim(), checkedItems, getPrice, getPeople, getRequirement, getFacilities, getlandmark, latitude.trim(), longitude.trim(),ownersuid, uri.toString());
                                        dataReference.child(nodeval).setValue(holder);


                                        Rooms.setText("");
                                        price.setText("");
                                        peoples.setText("");
                                        requirement.setText("");
                                        facilities.setText("");
                                        landmark.setText("");
                                        roomImg.setImageResource(R.drawable.ic_launcher_background);
//                                Toast.makeText(AddRooms.this, checkedItems, Toast.LENGTH_SHORT).show();

                                        Toast.makeText(AddRooms.this, "Room Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent = new Intent(AddRooms.this, Displayrooms.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                float uploadpercent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                dialog.setMessage("uploaded :" + (int) uploadpercent + " %");
                            }
                    });
        }

    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

    }

}