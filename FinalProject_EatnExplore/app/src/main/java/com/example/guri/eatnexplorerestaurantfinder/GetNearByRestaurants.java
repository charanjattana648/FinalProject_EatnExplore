package com.example.guri.eatnexplorerestaurantfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearByRestaurants extends AsyncTask<Object,String,String>  {



    private String googleRestaurantsData;
    private GoogleMap mMap;
    private String url;
    private MarkerOptions markerOptions;
    private LatLng latLng;
    private FragmentManager fragmentManager;
    private ResturantDetailFragment resturantDetailFragment;
    private FragmentTransaction fragmentTransaction;
    private double rating,lat,lng;
    private boolean isOpenStore;
    private int user_ratings_total;

    public GetNearByRestaurants(FragmentManager fragmentManager)
    {
        this.fragmentManager=fragmentManager;
    }


    @Override
    protected String doInBackground(Object... objects) {

        mMap=(GoogleMap)objects[0];
        url=(String)objects[1];

        DownloadUrl downloadUrl=new DownloadUrl();

        try {
            googleRestaurantsData=downloadUrl.readUrl(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleRestaurantsData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaceList;
        DataParser dataParser=new DataParser();
        nearbyPlaceList=dataParser.parse(s);
        Log.d("onPostExecutetesting11", ": called parse method"+nearbyPlaceList.toString());
        rLoadDetail();
        showNearbyPlaces(nearbyPlaceList);
    }

    private void showNearbyPlaces(final List<HashMap<String,String>> nearbyPlaceList)
    {
        for (int i=0;i<nearbyPlaceList.size();++i)
        {
            markerOptions=new MarkerOptions();
            HashMap<String,String> googlePlace=nearbyPlaceList.get(i);

            rating =Double.parseDouble(googlePlace.get("rating"));
            isOpenStore=Boolean.parseBoolean(googlePlace.get("open_now"));
            String placeName=googlePlace.get("name");
            String vicinity=googlePlace.get("vicinity");
            lat=Double.parseDouble(googlePlace.get("lat"));
            user_ratings_total=Integer.parseInt(googlePlace.get("user_ratings_total"));
            lng=Double.parseDouble(googlePlace.get("lng"));

            latLng=new LatLng(lat,lng);

            markerOptions.position(latLng);
            markerOptions.title(placeName+" : "+vicinity+" ");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));



            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    latLng=marker.getPosition();
                    markerOptions.position(marker.getPosition());
                    markerOptions.title(marker.getTitle());
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    Location location=HomeScreenMainActivity.currLocation;

                    Location currSelectedLocation=new Location(LocationManager.GPS_PROVIDER);
                    currSelectedLocation.setLatitude(latLng.latitude);
                    currSelectedLocation.setLongitude(latLng.longitude);
                    float distance= location.distanceTo(currSelectedLocation);

                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),15));

                    resturantDetailFragment=ResturantDetailFragment.newInstance();
                    int index=marker.getTitle().indexOf(":");
                    String resturantName=marker.getTitle().substring(0,index-1);

                    for (int i=0;i<nearbyPlaceList.size();++i)
                    {
                        HashMap<String,String> googlePlace=nearbyPlaceList.get(i);

                        if(googlePlace.get("name").equalsIgnoreCase(resturantName))
                        {
                            Log.d("loop testing", "onInfoWindowClick: entering");
                            user_ratings_total=Integer.parseInt(googlePlace.get("user_ratings_total"));
                            rating =Double.parseDouble(googlePlace.get("rating"));
                            isOpenStore=Boolean.parseBoolean(googlePlace.get("open_now"));
                        }

                    }

                    String rlocation=marker.getTitle().substring(index+1);
                    String userRating=String.valueOf(rating);
                    String votes=String.valueOf(user_ratings_total)+" voted";
                    String storeStatus ="";
                    if(isOpenStore) {
                        storeStatus ="Open";
                    }
                    else{
                        storeStatus ="Close";
                    }

                    DecimalFormat df=new DecimalFormat("##.##");
                    if(distance>1000)
                    {
                        distance=distance/1000;
                        resturantDetailFragment.loadData(resturantName,df.format(distance)+" km",rlocation,userRating,votes,storeStatus);
                    }else{
                        resturantDetailFragment.loadData(resturantName,df.format(distance)+" meter",rlocation,userRating,votes,storeStatus);
                    }



                }
            });
        }
    }

    //loading the ResturantDetailFragment
    public void rLoadDetail()
    {
        resturantDetailFragment=ResturantDetailFragment.newInstance();
        fragmentTransaction=fragmentManager.beginTransaction()
                .replace(R.id.resturant_detail_container,resturantDetailFragment)
                .addToBackStack(null);
        fragmentTransaction.commit();

    }




}
