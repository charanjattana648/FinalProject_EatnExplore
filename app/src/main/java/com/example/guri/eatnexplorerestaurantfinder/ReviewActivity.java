package com.example.guri.eatnexplorerestaurantfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private ListView listView;
    private ReviewListAdapter reviewListAdapter;
    private TextView resturantN_txt,noReview_txt;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Review> reviewList;
    private float rating;
    String restaurantEntered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        resturantN_txt=findViewById(R.id.textView_RName);
        listView = (ListView) findViewById(R.id.revRatList);
        noReview_txt=findViewById(R.id.noReview);
        reviewList= new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        Intent intent=getIntent();
        if(intent!=null)
        {
            restaurantEntered=intent.getStringExtra("resturantName");
        }
        databaseReference=firebaseDatabase.getReference("feedbackData").child(restaurantEntered);

        getReview();

    }


    public void getReview()
    {
        resturantN_txt.setText(restaurantEntered);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noReview_txt.setVisibility(View.GONE);
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Review review = dataSnapshot1.getValue(Review.class);

                    reviewList.add(new Review(review.getReview(),review.getStars(),review.getReviewDate(),review.getUserName(),review.getUId()));
                    reviewListAdapter=new ReviewListAdapter(getApplicationContext(),reviewList);
                    listView.setAdapter(reviewListAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("<<<<<<<<<<<<<<<", "onCancelled:enter --------");
            }
        });


    }
}
