package com.example.guri.eatnexplorerestaurantfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {

    Context context = this;
    TextView placeTv;
    Button submitBtn;
    EditText reviewEt;
    RatingBar ratingBar;
    String restaurantEntered;
    int votes=0;
    float ratingAverage=0;
    FirebaseAuth mAuth;
    FirebaseUser currUser;

    private DatabaseReference mDatabase, mDatabaseRestaurant, ratingDatabase;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mAuth=FirebaseAuth.getInstance();

        placeTv = (TextView)findViewById(R.id.placeTextView);
        submitBtn = (Button)findViewById(R.id.submitButton);
        reviewEt = (EditText) findViewById(R.id.reviewButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingStars);
        Intent intent=getIntent();
        if(intent!=null)
        {
            restaurantEntered=intent.getStringExtra("resturantName");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRestaurant = firebaseDatabase.getReference("feedbackData").child(restaurantEntered);

        placeTv.setText("Place:  "+restaurantEntered);


        mDatabase = firebaseDatabase.getReference("feedbackData").child(restaurantEntered);
        ratingDatabase = firebaseDatabase.getReference().child("Rating");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Review review1 = dataSnapshot1.getValue(Review.class);
                    votes=(int)dataSnapshot.getChildrenCount();

                    ratingAverage +=review1.stars;
                    Log.d("counttttt", "onDataChange: "+dataSnapshot.getChildrenCount()+" -stars- "+review1.getStars());
                }
                if(votes!=0){
                    Log.d("counttttt", "onDataChange: avg "+ratingAverage);
                    ratingAverage = ratingAverage/votes;
                    Log.d("counttttt", "onDataChange: avg 2 "+ratingAverage);
                    DecimalFormat df = new DecimalFormat("##.##");
                    ratingAverage=Float.parseFloat(df.format(ratingAverage));
                    df.setMaximumFractionDigits(2);
                    ratingDatabase.child(restaurantEntered).child("stars").setValue(ratingAverage);
                    ratingDatabase.child(restaurantEntered).child("votes").setValue(votes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = reviewEt.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                Log.d(">>>>>>>>", "onClick: "+ currentTime.getDate());
                String uName=mAuth.getCurrentUser().getDisplayName();
                Log.d("<<<<<<<<<>>", "onClick: "+mAuth.getCurrentUser().getEmail());
                if(uName.isEmpty())
                {
                    uName=mAuth.getCurrentUser().getEmail();
                }
                String uId=mAuth.getCurrentUser().getUid();
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
               // simpleDateFormat.format(currentTime);
                String reviewDate=dateFormat.format(currentTime)+" "+timeFormat.format(currentTime.getTime());
                Log.d(">>>>>>>>1", "onClick: "+dateFormat.format(currentTime)+" --- "+timeFormat.format(currentTime.getTime()));
                float stars = ratingBar.getRating();
                Review obj = new Review(review, stars,reviewDate,uName,uId);
                mDatabaseRestaurant.push().setValue(obj);
                Toast.makeText(context, "Thanks, Your Review Submitted!!", Toast.LENGTH_SHORT).show();
                reviewEt.setText("");
                startActivity(new Intent(FeedbackActivity.this, ListRestaurants.class));
            }
        });
    }
}
