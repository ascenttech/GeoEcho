package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.util.Constants;

import java.util.List;

/**
 * Created by ADMIN on 20-06-2015.
 */
public class SplashScreenActivity extends Activity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int login;
    Location finalLoc = null,net_loc = null,gps_loc = null;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.LOG_TAG, Constants.SplashScreenActivity);

        setContentView(R.layout.activity_splash_screen);

        Thread timerThread = new Thread(){
            public void run(){
                try{


                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Log.d(Constants.LOG_TAG," Location Manager "+ lm);

                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    Log.d(Constants.LOG_TAG," GPS enabled "+ gps_enabled);

                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    Log.d(Constants.LOG_TAG," Network Enable "+ network_enabled);

                    if (gps_enabled){
                        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d(Constants.LOG_TAG," Location "+ gps_loc);
                    }
                    if (network_enabled){
                        net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.d(Constants.LOG_TAG," Location from Network "+ net_loc);
                    }
                    if (gps_loc != null && net_loc != null) {
                        if (gps_loc.getAccuracy() >= net_loc.getAccuracy())
                            finalLoc = gps_loc;
                        else
                            finalLoc = net_loc;
                    }
                    else {
                        if (gps_loc != null) {
                            finalLoc = gps_loc;
                        } else if (net_loc != null) {
                            finalLoc = net_loc;
                        }
                    }
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//                    editor = pref.edit();
//                    editor.commit();
                    login = pref.getInt("login",1);
                    if(login == 0) {
                        Intent i = new Intent(SplashScreenActivity.this, LandingActivity.class);
                        i.putExtra("latitude",finalLoc.getLatitude());
                        i.putExtra("longitude",finalLoc.getLongitude());
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            }
        };
        timerThread.start();
    }

}
