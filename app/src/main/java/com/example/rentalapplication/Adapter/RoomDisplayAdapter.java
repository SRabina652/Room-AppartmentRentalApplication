package com.example.rentalapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalapplication.R;
import com.example.rentalapplication.SingleRoomDisplayActivity;
import com.example.rentalapplication.model.Payment;
import com.example.rentalapplication.model.addRoomDataHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.widget.KhaltiButton;


import java.util.ArrayList;
import java.util.Map;

public class RoomDisplayAdapter extends RecyclerView.Adapter<RoomDisplayAdapter.displayViewHolder> {

    ArrayList<addRoomDataHolder> roomsData;
    Context context;

    String customersUid = "";

    public RoomDisplayAdapter(ArrayList<addRoomDataHolder> roomsData, Context context) {
        this.roomsData = roomsData;
        this.context = context;
    }

    public RoomDisplayAdapter(ArrayList<addRoomDataHolder> roomsData, Context context, String customersUid) {
        this.roomsData = roomsData;
        this.customersUid = customersUid;
        this.context = context;
    }

    @NonNull
    @Override
    public displayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_display_room, parent, false);
        return new displayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull displayViewHolder holder, int position) {
        holder.noOfRooms.setText(roomsData.get(position).getRooms());
        holder.landmarkdisplay.setText(roomsData.get(position).getLandmark());
        holder.Roomprice.setText(roomsData.get(position).getPrice());
        if (roomsData.get(position).getBookedOrNot().equals("true")) {
            holder.bookedOrNot.setText("This Room Is Already Booked");
        } else if (roomsData.get(position).getBookedOrNot().equals("false")) {
            holder.bookedOrNot.setText("You can book this room");
        } else {
            holder.bookedOrNot.setText("Please Consult the owner before payment");
        }

        Glide.with(holder.imgDisplay.getContext()).load(roomsData.get(position).getRoomImg()).into(holder.imgDisplay);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleRoomDisplayActivity.class);
                intent.putExtra("rooms", roomsData.get(position).getRooms());
                intent.putExtra("landmark", roomsData.get(position).getLandmark());
                intent.putExtra("price", roomsData.get(position).getPrice());
                intent.putExtra("images", roomsData.get(position).getRoomImg());
                intent.putExtra("people", roomsData.get(position).getPeoples());
                intent.putExtra("typeOfAppliers", roomsData.get(position).getCheckedItems());
                intent.putExtra("facilities", roomsData.get(position).getFacilities());
                intent.putExtra("requirements", roomsData.get(position).getRequirement());
                intent.putExtra("latitude", roomsData.get(position).getLatitude());
                intent.putExtra("longitude", roomsData.get(position).getLongitude());
                intent.putExtra("RandomNumber", roomsData.get(position).getRandomNumber());
                intent.putExtra("ownersuid", roomsData.get(position).getOwnersuid());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if (roomsData.get(position).getBookedOrNot().equals("false")) {
            holder.khaltiButton.setVisibility(View.VISIBLE);
        } else {
            holder.khaltiButton.setVisibility(View.GONE);
        }

        khaltiPayment(holder.itemView.getContext(), holder.khaltiButton, roomsData.get(position).getRandomNumber().trim(), "RentPayment", roomsData.get(position).getPrice().trim(), roomsData.get(position).getRandomNumber());


    }

    @Override
    public int getItemCount() {
        return roomsData.size();
    }

    public void khaltiPayment(Context mctx, KhaltiButton khaltiButton, String productId, String productName, String price, String getRandomNumber) {
        Long changePrice = Long.parseLong(price);

        String publicKey = "test_public_key_3a1648884bcb469ea86c5dc823143469";
        Config.Builder builder = new Config.Builder(publicKey, productId, productName, changePrice, new OnCheckOutListener() {
            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());

                FirebaseDatabase FBdatabase = FirebaseDatabase.getInstance();
                DatabaseReference dataReference = FBdatabase.getReference("Rooms");


                Payment payment = new Payment(customersUid, changePrice.toString());
                dataReference.child(getRandomNumber).child("Payment").child(customersUid).setValue(payment);
                updateData();
            }

            private void updateData() {

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Rooms").child(getRandomNumber).child("bookedOrNot");


                // Update the data
                databaseRef.setValue("true")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Data updated successfully
                                Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to update data
                                Toast.makeText(context, "Failed to update data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i("action", errorMap.toString());
                Toast.makeText(mctx, errorMap.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Config config = builder.build();
        khaltiButton.setCheckOutConfig(config);
        KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(mctx, config);
        khaltiButton.setOnClickListener(v -> khaltiCheckOut1.show());
    }

    public class displayViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDisplay;
        TextView noOfRooms, landmarkdisplay, Roomprice, bookedOrNot;
        KhaltiButton khaltiButton;

        public displayViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
            noOfRooms = (TextView) itemView.findViewById(R.id.noOfRooms);
            landmarkdisplay = (TextView) itemView.findViewById(R.id.landmarkdisplay);
            Roomprice = (TextView) itemView.findViewById(R.id.Roomprice);
            bookedOrNot = (TextView) itemView.findViewById(R.id.bookedOrNot);
            khaltiButton = itemView.findViewById(R.id.kpay);
        }
    }
}



