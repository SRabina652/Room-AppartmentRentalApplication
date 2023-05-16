package com.example.rentalapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalapplication.R;
import com.example.rentalapplication.model.RatingAndReviewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RatingAndReviewAdapter extends FirebaseRecyclerAdapter<RatingAndReviewModel,RatingAndReviewAdapter.ratingReviewViewHolder> {

   Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RatingAndReviewAdapter(@NonNull FirebaseRecyclerOptions<RatingAndReviewModel> options, Context context) {
        super(options);
        this.context=context;
    }


    @NonNull
    @Override
    public ratingReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singledisplayreviewandrating,parent,false);
        return new ratingReviewViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ratingReviewViewHolder holder, int position, @NonNull RatingAndReviewModel model) {
        holder.DisplayReview.setText(model.getReview());
        float ratingValue=model.getRating();
        holder.displayRatingBar.setRating(ratingValue);
        holder.name.setText(model.getName());
    }




    public class ratingReviewViewHolder extends RecyclerView.ViewHolder{
        TextView name,DisplayReview;

        RatingBar displayRatingBar;
        public ratingReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.displayRatingUserName);
            displayRatingBar = (RatingBar)itemView.findViewById(R.id.displayuserRatingAndReview);
            DisplayReview = (TextView)itemView.findViewById(R.id.DisplayUsersReview);
        }
    }

}
