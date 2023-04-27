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
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

public class AddRooms extends AppCompatActivity {
    static Integer node=0;

    String checkedItems="";
    CheckBox checkbox_Student, checkbox_Family,checkbox_women,checkbox_men;
    TextInputEditText price, peoples, requirement,facilities,Rooms;
    EditText landmark, Location;
    ImageView roomImg;
    Uri fPath;
    Button add;
    Bitmap bmap;

    private static final int STORAGE_PERMISSION_CODE=100;
    private static final String tag = "PERMISSION_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);

        //checkboxs

        checkbox_Student= findViewById(R.id.checkbox_Student);
        checkbox_Family= findViewById(R.id.checkbox_Family);
        checkbox_women = findViewById(R.id.checkbox_women);
        checkbox_men = findViewById(R.id.checkbox_men);



        Rooms=findViewById(R.id.Rooms);


        roomImg = findViewById(R.id.roomImg);

        price = findViewById(R.id.price);
        peoples = findViewById(R.id.peoples);
        requirement = findViewById(R.id.requirement);
        facilities = findViewById(R.id.facilities);

        landmark = findViewById(R.id.plandmark);
        Location = findViewById(R.id.pLocation);

        add= findViewById(R.id.roomsubmit);



                ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if(result.getResultCode() == RESULT_OK){
                                    Intent data =result.getData();
                                    fPath = data.getData();
                                    try{
                                        InputStream inputStream = getContentResolver().openInputStream(fPath);
                                        bmap= BitmapFactory.decodeStream(inputStream);
                                        roomImg.setImageBitmap(bmap);
                                    }catch (Exception e){

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
        if(requestCode == 10 && resultCode == RESULT_OK){
            fPath = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(fPath);
                bmap= BitmapFactory.decodeStream(inputStream);
                roomImg.setImageBitmap(bmap);
            }catch (Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadDataToFirebase() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Data Uploading");
        dialog.show();

        FirebaseStorage dataStorage =FirebaseStorage.getInstance();
        StorageReference dataUploader = dataStorage.getReference("roomImage"+ new Random().nextInt(50));

        String student="Student";
        String family = "Family";
        String women= "Women";
        String men = "Men";


        if(checkbox_Student.isChecked()){
            checkedItems +=student;
        }
        if (checkbox_Family.isChecked()) {
            checkedItems = checkedItems + ", " + family;
        }
        if (checkbox_women.isChecked()){
            checkedItems = checkedItems + ", " + women;
        }
        if (checkbox_men.isChecked()) {
            checkedItems = checkedItems + ", " + men;
        }

        dataUploader.putFile(fPath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dataUploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
                                DatabaseReference dataReference = FBdatabase.getReference("Rooms");
                                String search=Rooms.getText().toString().trim() + " "+checkedItems+ " " + price.getText().toString().trim() +" "+ peoples.getText().toString().trim() + " "+ facilities.getText().toString().trim() + " " + landmark.getText().toString().trim() + " "+Location.getText().toString().trim();
                                addRoomDataHolder holder= new addRoomDataHolder(search,Rooms.getText().toString().trim(),checkedItems,price.getText().toString().trim(),peoples.getText().toString().trim(),requirement.getText().toString().trim(),facilities.getText().toString().trim(),landmark.getText().toString().trim(),Location.getText().toString().trim(),uri.toString());

                                    String nodeval = UUID.randomUUID().toString().replaceAll("-", "");
                                    dataReference.child(nodeval).setValue(holder);
                                    Rooms.setText("");
                                    price.setText("");
                                    peoples.setText("");
                                    requirement.setText("");
                                    facilities.setText("");
                                    landmark.setText("");
                                    Location.setText("");
                                    roomImg.setImageResource(R.drawable.ic_launcher_background);
//                                Toast.makeText(AddRooms.this, checkedItems, Toast.LENGTH_SHORT).show();

                                    Toast.makeText(AddRooms.this, "Room Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float uploadpercent=(100* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("uploaded :"+ (int)uploadpercent + " %");
                    }
                });
    }
}