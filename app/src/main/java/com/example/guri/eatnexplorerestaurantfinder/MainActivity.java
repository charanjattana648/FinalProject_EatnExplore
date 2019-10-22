package com.example.guri.eatnexplorerestaurantfinder;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
ImageView imgIcon,imageName;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageName=findViewById(R.id.imageView_name);
        imgIcon=findViewById(R.id.imageview_icon);



        new Thread(new Runnable() {
            @Override
            public void run() {

                while(i<4)
                {
                    SystemClock.sleep(1000);
                    i++;

                        imageName.post(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("tt", "run: "+i);
                             if(i%2!=0) {
                                 //move from x to y
                                 Animation animation = new TranslateAnimation(0, 1000,0, 0);
                                 animation.setDuration(1000);
                                 animation.setFillAfter(true);
                                 imageName.startAnimation(animation);
                                 imageName.setVisibility(View.VISIBLE);

                                 //rotate image icon
                                 Animation animation2 = new RotateAnimation(0, 360, 0, 0);
                                 animation2.setDuration(1200);
                                 animation2.setFillAfter(true);
                                 imgIcon.startAnimation(animation2);
                                 imgIcon.setVisibility(View.VISIBLE);


                             }
                             else{
                                 //move from x to y to bring to old pos
                                 Animation animation1 = new TranslateAnimation(0, 10, 0, 0);
                                 animation1.setDuration(1000);
                                 animation1.setFillAfter(true);
                                 imageName.startAnimation(animation1);
                                 imageName.setVisibility(View.VISIBLE);
                             }
                            }
                        });
                    }  startActivity(new Intent(MainActivity.this, HomeScreenMainActivity.class));

            }
        }).start();
    }

}
