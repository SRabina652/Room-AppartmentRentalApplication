package com.example.rentalapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DisplayRoomAdapter extends FirebaseRecyclerAdapter<addRoomDataHolder, DisplayRoomAdapter.displayViewHolder> {

    Context context;

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
        Glide.with(holder.imgDisplay.getContext()).load(model.getRoomImg()).into(holder.imgDisplay);
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context,SingleRoomDisplayActivity.class);
                intent.putExtra("rooms", model.getRooms());
                intent.putExtra("landmark", model.getLandmark());
                intent.putExtra("price", model.getPrice());
                intent.putExtra("images", model.getRoomImg());
                intent.putExtra("people", model.getPeoples());
                intent.putExtra("typeOfAppliers", model.getCheckedItems());
                intent.putExtra("facilities", model.getFacilities());
                intent.putExtra("requirements", model.getRequirement());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public displayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow_display_room,parent,false);
        return new displayViewHolder(view);
    }

    public class displayViewHolder extends RecyclerView.ViewHolder{

        ImageView imgDisplay;
        TextView noOfRooms,landmarkdisplay, Roomprice, seeMore;
        public displayViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDisplay = (ImageView) itemView.findViewById(R.id.imgDisplay);
            noOfRooms = (TextView)itemView.findViewById(R.id.noOfRooms);
            landmarkdisplay = (TextView)itemView.findViewById(R.id.landmarkdisplay);
            Roomprice = (TextView)itemView.findViewById(R.id.Roomprice);
            seeMore = (TextView)itemView.findViewById(R.id.seeMore);

        }
    }
}
