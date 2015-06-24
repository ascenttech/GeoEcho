package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.util.Constants;

/**
 * Created by ADMIN on 20-06-2015.
 */
public class SplashScreenActivity extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.LOG_TAG,Constants.SplashScreenActivity);

        setContentView(R.layout.activity_splashscreen);
        
        Thread timerThread = new Thread(){
            public void run(){
                try{

                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent i = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(i);
                }
            }
        };
        timerThread.start();
    }

}
