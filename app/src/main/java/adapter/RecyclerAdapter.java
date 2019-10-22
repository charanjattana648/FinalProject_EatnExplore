package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guri.eatnexplorerestaurantfinder.FeedbackActivity;
import com.example.guri.eatnexplorerestaurantfinder.R;
import com.example.guri.eatnexplorerestaurantfinder.ReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import bean.Item;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BusinessViewHolder>{
    List<Item> businessList;
    Context ctx;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;



    public RecyclerAdapter(List<Item> businessList, Context ctx, RecyclerView recyclerView) {
        this.businessList = businessList;
        this.ctx = ctx;
        this.recyclerView = recyclerView;
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);

        // view.setOnClickListener(onClickListener);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BusinessViewHolder businessViewHolder, int i) {
        businessViewHolder.txtName.setText(businessList.get(i).getName());
        businessViewHolder.txtPhone.setText(businessList.get(i).getPhone());
        //  businessViewHolder.txtAvgRating.setText(businessList.get(i).getAvgRating());
        businessViewHolder.txtReviewCount.setText(businessList.get(i).getReviewsCount());
        businessViewHolder.txtAddress.setText(businessList.get(i).getAddress());

        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";

        //businessViewHolder.ratingBar.setMax(5);
        businessViewHolder.ratingBar.setNumStars(Integer.parseInt(businessList.get(i).getAvgRating()));

        Picasso.get().load(businessList.get(i).getImageUrl()).into(businessViewHolder.imgView);

        final int pos = i;
        final Context context = ctx;
        businessViewHolder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = businessList.get(pos).getName();
                if(currentUser!=null) {
                    Intent feedbackIntent = new Intent(context, FeedbackActivity.class).putExtra("resturantName", name);
                    context.startActivity(feedbackIntent);
                }
                else {
                    Toast.makeText(context, "Please SignIn First", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //
        businessViewHolder.viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = businessList.get(pos).getName();
                if(currentUser!=null) {
                    Intent feedbackIntent = new Intent(context, ReviewActivity.class).putExtra("resturantName", name);
                    context.startActivity(feedbackIntent);
                }
                else {
                    Toast.makeText(context, "Please SignIn First", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public static class BusinessViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtAvgRating, txtReviewCount, txtAddress, txtPhone;
        ImageView imgView;
        RatingBar ratingBar;
        Button btnFeedback,viewFeedbackBtn;
        public BusinessViewHolder(final View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAvgRating = (TextView) view.findViewById(R.id.txtAvgRating);
            txtReviewCount = (TextView) view.findViewById(R.id.txtReviewCount);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            imgView = (ImageView) view.findViewById(R.id.imgView);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            btnFeedback = (Button)view.findViewById(R.id.btnFeedback);
            viewFeedbackBtn=view.findViewById(R.id.btnFeedbackView);
            btnFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context=view.getContext();
                    Intent feedbackIntent=new Intent(context, FeedbackActivity.class);
                    context.startActivity(feedbackIntent);
                }
            });
        }
    }
}
