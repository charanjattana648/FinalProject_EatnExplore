package com.example.guri.eatnexplorerestaurantfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* TimerTask task = new TimerTask() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(MainActivity.this, HomeScreenMainActivity.class));
            }
        };
        Timer opening = new Timer();
        opening.schedule(task,3000);*/
        SplashTask splashTask=new SplashTask();
        splashTask.execute();
    }
    private class SplashTask extends AsyncTask<Void,Integer,Integer>
    {

        @Override
        protected Integer doInBackground(Void... voids) {
            int i=0;
            while (i<3)
            {
                SystemClock.sleep(1000);
                ++i;
            }
            return i;
        }

     

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            startActivity(new Intent(MainActivity.this, HomeScreenMainActivity.class));
        }
    }
}
