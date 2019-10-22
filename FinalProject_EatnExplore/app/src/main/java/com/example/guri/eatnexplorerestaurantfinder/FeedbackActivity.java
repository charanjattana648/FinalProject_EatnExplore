package com.example.guri.eatnexplorerestaurantfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    Context context = this;
    TextView placeTv;
    Button submitBtn;
    EditText reviewEt;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        placeTv = (TextView)findViewById(R.id.placeTextView);
        submitBtn = (Button)findViewById(R.id.submitButton);
        reviewEt = (EditText) findViewById(R.id.reviewButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingStars);

        reviewEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View reviewPrompt = layoutInflater.inflate(R.layout.reviews_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(reviewPrompt);

                final EditText userInput = (EditText)reviewPrompt.findViewById(R.id.reviewEditText);

                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reviewEt.setText(userInput.getText());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = reviewEt.getText().toString();
                Toast.makeText(FeedbackActivity.this, "Review: "+review+", rating: "+ratingBar.getRating()+"", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
