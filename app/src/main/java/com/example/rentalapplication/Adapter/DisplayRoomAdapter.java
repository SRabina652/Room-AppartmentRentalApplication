package com.example.rentalapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalapplication.R;
import com.example.rentalapplication.SingleRoomDisplayActivity;
import com.example.rentalapplication.model.addRoomDataHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.Map;

//this adapter was used before while using the firebase search but not now
public class DisplayRoomAdapter extends FirebaseRecyclerAdapter<addRoomDataHolder, DisplayRoomAdapter.displayViewHolder> {

    Context context;
    Boolean bookedOrNot=false;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DisplayRoomAdapter(@NonNull FirebaseRecyclerOptions<addRoomDataHolder> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull displayViewHolder holder, int position, @NonNull addRoomDataHolder model) {

        holder.noOfRooms.setText(model.getRooms());
        holder.landmarkdisplay.setText(model.getLandmark());
        holder.Roomprice.setText(model.getPrice());
        if(bookedOrNot){
            holder.bookedOrNot.setText("This Room Is Already Booked");
        }else{
            holder.bookedOrNot.setText("this is the place for uid");
        }

        Glide.with(holder.imgDisplay.getContext()).load(model.getRoomImg()).into(holder.imgDisplay);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, SingleRoomDisplayActivity.class);
                intent.putExtra("rooms", model.getRooms());
                intent.putExtra("landmark", model.getLandmark());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("images", model.getRoomImg());
                intent.putExtra("people", model.getPeoples());
                intent.putExtra("typeOfAppliers", model.getCheckedItems());
                intent.putExtra("facilities", model.getFacilities());
                intent.putExtra("requirements", model.getRequirement());
                intent.putExtra("latitude",model.getLatitude());
                intent.putExtra("longitude",model.getLongitude());
                intent.putExtra("RandomNumber",model.getRandomNumber());
                intent.putExtra("ownersuid",model.getOwnersuid());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        khaltiPayment(holder.itemView.getContext(),holder.kpay,model.getRandomNumber(),model.getLandmark(),model.getPrice());

    }

    @NonNull
    @Override
    public displayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_display_room,parent,false);
        return new displayViewHolder(view);
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

                //you have to perform some operation here
//                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Rooms");
//                databaseRef.setValue(bookedOrNot);

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

    public class displayViewHolder extends RecyclerView.ViewHolder{

        ImageView imgDisplay;
        TextView noOfRooms,landmarkdisplay, Roomprice, bookedOrNot;

        KhaltiButton kpay;
        public displayViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
            noOfRooms = (TextView)itemView.findViewById(R.id.noOfRooms);
            landmarkdisplay = (TextView)itemView.findViewById(R.id.landmarkdisplay);
            Roomprice = (TextView)itemView.findViewById(R.id.Roomprice);
            bookedOrNot = (TextView)itemView.findViewById(R.id.bookedOrNot);
            kpay = itemView.findViewById(R.id.kpay);
        }
    }
}
