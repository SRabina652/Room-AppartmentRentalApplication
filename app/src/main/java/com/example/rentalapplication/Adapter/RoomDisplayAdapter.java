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
import com.example.rentalapplication.model.addRoomDataHolder;
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

    Boolean bookedOrNot=false;

    public RoomDisplayAdapter(ArrayList<addRoomDataHolder> roomsData, Context context) {
        this.roomsData = roomsData;
        this.context = context;
    }

    @NonNull
    @Override
    public displayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_display_room,parent,false);
       return new displayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull displayViewHolder holder, int position) {
        holder.noOfRooms.setText(roomsData.get(position).getRooms());
        holder.landmarkdisplay.setText(roomsData.get(position).getLandmark());
        holder.Roomprice.setText(roomsData.get(position).getPrice());
        if(bookedOrNot){
            holder.bookedOrNot.setText("This Room Is Already Booked");
        }else{
            holder.bookedOrNot.setText("You can book this room");
        }

        Glide.with(holder.imgDisplay.getContext()).load(roomsData.get(position).getRoomImg()).into(holder.imgDisplay);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, SingleRoomDisplayActivity.class);
                intent.putExtra("rooms", roomsData.get(position).getRooms());
                intent.putExtra("landmark", roomsData.get(position).getLandmark());
                intent.putExtra("price", roomsData.get(position).getPrice());
                intent.putExtra("images", roomsData.get(position).getRoomImg());
                intent.putExtra("people", roomsData.get(position).getPeoples());
                intent.putExtra("typeOfAppliers", roomsData.get(position).getCheckedItems());
                intent.putExtra("facilities", roomsData.get(position).getFacilities());
                intent.putExtra("requirements", roomsData.get(position).getRequirement());
                intent.putExtra("latitude",roomsData.get(position).getLatitude());
                intent.putExtra("longitude",roomsData.get(position).getLongitude());
                intent.putExtra("RandomNumber",roomsData.get(position).getRandomNumber());
                intent.putExtra("ownersuid",roomsData.get(position).getOwnersuid());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        khaltiPayment(holder.itemView.getContext(),holder.kpay,roomsData.get(position).getRandomNumber(),roomsData.get(position).getLandmark(),roomsData.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return roomsData.size();
    }
    public void khaltiPayment(Context mctx,KhaltiButton khaltiButton,String productId,String productName,String price){
        Config.Builder builder = new Config.Builder(Constant.pub, "Product ID", "Main", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
                Toast.makeText(mctx, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                bookedOrNot=true;

            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.EBANKING);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                    add(PaymentPreference.SCT);
                }});

        Config config = builder.build();
        khaltiButton.setCheckOutConfig(config);

        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(mctx, config);
        khaltiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                khaltiCheckOut.show();
            }
        });
    }

    public class displayViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDisplay;
        TextView noOfRooms, landmarkdisplay, Roomprice, bookedOrNot;
        KhaltiButton kpay;

        public displayViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
            noOfRooms = (TextView) itemView.findViewById(R.id.noOfRooms);
            landmarkdisplay = (TextView) itemView.findViewById(R.id.landmarkdisplay);
            Roomprice = (TextView) itemView.findViewById(R.id.Roomprice);
            bookedOrNot = (TextView) itemView.findViewById(R.id.bookedOrNot);
            kpay = itemView.findViewById(R.id.kpay);

        }
    }
}
