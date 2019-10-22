package com.example.guri.eatnexplorerestaurantfinder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.RecyclerAdapter;
import bean.Item;

public class ListRestaurants extends AppCompatActivity {

    /* EditText edtSearchTag, edtLocation;
     Button btnSearch;*/
    Context ctx;
    String searchTag="", location="";
    String responseGlobal;
    ArrayList<Item> businessList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrestaurants);

        ctx = this;

        // fetchData();

        getIds();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tempSearchTag, tempLocation;

        tempSearchTag=sharedPreferences.getString("searchTag", "");
        tempLocation = sharedPreferences.getString("location", "");
        if(tempSearchTag.equals("") || tempSearchTag==null)
        {
            searchTag =  "Indian restaurant";

        }
        else
        {
            searchTag = tempSearchTag + " restaurant";
        }
        if(tempLocation.equals("") || tempLocation==null)
        {
            location = "nyc";
        }
        else {
            location = tempLocation;
        }
        //searchTag =  "Indian restaurant";
        //location = "nyc";
        fetchData();


        //  setListeners();

    }





    final static String ACCESS_TOKEN = "JJI-3JwkDPpN8amJfCLElURIKsbodnkcFx_sOnxAgil-fFb6kEFqurL14e03y-53q4XKbGIaJLTNI_l99u0ZHjl1uh1TchIiaFXUUV8lQsn-voarqog_P9cx2a1XXHYx";

    public void fetchData() {

        //   dataList.clear();
        RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(ctx);
        //String url = "https://api.yelp.com/v3/businesses/search?term=%22restaurant%22&location=%22NYC%22";

        String url = "https://api.yelp.com/v3/businesses/search?term=%22"+ searchTag +"%22&location=%22" + location +"%22";


        //  String url = "https://api.yelp.com/v3/businesses/search?";
        //String url="https://practicetest.icbc.com/data/opkt/english.xml";
        final ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Fetching The List...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            responseGlobal = response;
                            System.out.println("<<<<<<<" + response.toString());
                            System.out.println("<<<<<<<" + response.toString().length());
                            //dataList = Parser.parseIt(response.toString());
//                    /        Log.d("size", "sizeofdataListFromVolley" + dataList.size());
                            // createRecyclerViewAndSetAdapter(ctx, recyclerView);
                            //  DBHelper.addQuestionsToDB(dataList);
                            progressDialog.dismiss();
                            parseData();

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                // params.put("Bearer", ACCESS_TOKEN);
                //params.put("token", ACCESS_TOKEN);
                params.put("Authorization", "Bearer JJI-3JwkDPpN8amJfCLElURIKsbodnkcFx_sOnxAgil-fFb6kEFqurL14e03y-53q4XKbGIaJLTNI_l99u0ZHjl1uh1TchIiaFXUUV8lQsn-voarqog_P9cx2a1XXHYx");
                return params;

            }


        };
        queue.add(stringRequest);
    }

    public void getIds()
    {
/*edtSearchTag = (EditText)findViewById(R.id.edtSearchTag);
    edtLocation = (EditText)findViewById(R.id.edtLocation);
    btnSearch = (Button)findViewById(R.id.btnSearch);*/
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

/*public void setListeners()
{
    btnSearch.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchTag = edtSearchTag.getText().toString() + " restaurant";
            location = edtLocation.getText().toString();
            fetchData();
            hideKeyboard(ListRestaurants.this);
        }
    });
}*/

    public void parseData()
    {

        businessList.clear();
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(responseGlobal);
            System.out.println("<<<<<<<<<<<<<<>>>>>>>>>>>>>>" + jsonObj.getJSONArray("businesses").length());
            JSONArray businessArray = jsonObj.getJSONArray("businesses");
            Item item;
            for (int i = 0; i < businessArray.length(); i++) {
                item = new Item();
                item.setName(businessArray.getJSONObject(i).getString("name"));
                item.setImageUrl(businessArray.getJSONObject(i).getString("image_url"));
                item.setReviewsCount(Integer.toString(businessArray.getJSONObject(i).getInt("review_count")));

                item.setAvgRating(Integer.toString(businessArray.getJSONObject(i).getInt("rating")));
                item.setPhone(businessArray.getJSONObject(i).getString("display_phone"));
                //item.setAddress(businessArray.getJSONObject(i).getJSONObject("location").getJSONArray("display_address").getJSONObject(0).toString());

                item.setAddress(businessArray.getJSONObject(i).getJSONObject("location").getJSONArray("display_address").getString(0) +
                        " " + businessArray.getJSONObject(i).getJSONObject("location").getJSONArray("display_address").getString(1));


                businessList.add(item);
            }

            System.out.println("length>>>>" + businessList.size());

            createRecyclerViewAndSetAdapter();

        } catch (Exception e) {

        }

        //for(int i=0;i<)


    }

    public void createRecyclerViewAndSetAdapter() {
        // Toast.makeText(ctx, businessList.size()+"createRecyclerViewAndSetAdapter",Toast.LENGTH_SHORT).show();
        recyclerAdapter = new RecyclerAdapter(businessList, this, recyclerView);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

    }
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
