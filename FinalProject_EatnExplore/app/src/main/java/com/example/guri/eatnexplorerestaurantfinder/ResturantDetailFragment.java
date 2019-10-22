
package com.example.guri.eatnexplorerestaurantfinder;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.reflect.Field;


    /**
     * A simple {@link Fragment} subclass.
     */
    public class ResturantDetailFragment extends Fragment {
        public  static TextView nametxt,distanceTxt,locTxt,statusTxt,ratingTxt,votesTxt;
        private ImageView imgResturant;
        private static View view;
        private static RatingBar ratingBar;



        public ResturantDetailFragment() {
        }

        public static ResturantDetailFragment newInstance() {


            return new ResturantDetailFragment();
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            view=inflater.inflate(R.layout.fragment_resturant_detail, container, false);
            nametxt=view.findViewById(R.id.txtName);
            distanceTxt=view.findViewById(R.id.txtDistance);
            locTxt=view.findViewById(R.id.txtLocation);
            statusTxt=view.findViewById(R.id.txtStatus);
            ratingTxt=view.findViewById(R.id.txtRating);
            votesTxt=view.findViewById(R.id.txtVotes);
            ratingBar=view.findViewById(R.id.ratingbarR);
            view.setVisibility(View.GONE);
            return view;
        }

        public void loadData(String name,String distance,String location,String rating, String votes,String status)
        {
            view.setVisibility(View.VISIBLE);
            nametxt.setText("Resturantt Name : "+name);
            locTxt.setText("Location : "+location);
            statusTxt.setText("Store : "+status);
            distanceTxt.setText("Distance : "+distance);
            ratingBar.setRating(Float.parseFloat(rating));
            ratingTxt.setText("Rating : "+rating);
            votesTxt.setText("Votes : "+votes);

        }
    }
