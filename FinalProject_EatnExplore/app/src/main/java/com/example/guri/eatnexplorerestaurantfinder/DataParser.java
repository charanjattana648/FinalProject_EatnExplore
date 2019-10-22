package com.example.guri.eatnexplorerestaurantfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String,String> getPlace(JSONObject jsonObject)
    {
        HashMap<String,String> googleRestaurantMap=new HashMap<>();
        String placeName="-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String reference="";
        String icon="";
        boolean open_store=false;
        String storeOpen="";

        String rating="";
        String user_ratings_total="";

        try {
            if(!jsonObject.isNull("name")) {
                placeName = jsonObject.getString("name");
            }
            if(!jsonObject.isNull("vicinity"))
            {
                vicinity=jsonObject.getString("vicinity");
            }
            latitude=jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference=jsonObject.getString("reference");
            icon=jsonObject.getString("icon");
            if(!jsonObject.isNull("opening_hours"))
            {
                if (!jsonObject.getJSONObject("opening_hours").isNull("open_now")) {
                    open_store = jsonObject.getJSONObject("opening_hours").getBoolean("open_now");
                }
            }
            storeOpen=Boolean.toString(open_store);
            //Log.d("open_now", "getPlace: "+open_now);
            rating=jsonObject.getString("rating");
            user_ratings_total=jsonObject.getString("user_ratings_total");

            googleRestaurantMap.put("name",placeName);
            googleRestaurantMap.put("vicinity",vicinity);
            googleRestaurantMap.put("lat",latitude);
            googleRestaurantMap.put("lng",longitude);
            googleRestaurantMap.put("reference",reference);
            googleRestaurantMap.put("icon",icon);
            googleRestaurantMap.put("open_now",storeOpen);
            googleRestaurantMap.put("rating",rating);
            googleRestaurantMap.put("user_ratings_total",user_ratings_total);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("testingData111", "getPlace: "+googleRestaurantMap.toString());

        return googleRestaurantMap;
    }


    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count =jsonArray.length();
        List<HashMap<String,String>> restaurantList=new ArrayList<>();
        HashMap<String,String> restaurantMap=null;

        for(int i=0;i<count;++i)
        {
            try {
                restaurantMap=getPlace((JSONObject)jsonArray.get(i));
                restaurantList.add(restaurantMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return restaurantList;
    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONObject jsonObject;
        JSONArray jsonArray=null;
        Log.d("DataParse..", "parse: "+jsonData);

        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("data", "parse: "+jsonArray.toString());
        return getPlaces(jsonArray);
    }
}
