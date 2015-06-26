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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.LOG_TAG, Constants.SplashScreenActivity);

        setContentView(R.layout.activity_splash_screen);
        
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    Log.d("geoecho","SplashScreenActivity");

                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (gps_enabled)
                        gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (network_enabled)
                        net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (gps_loc != null && net_loc != null) {

                        if (gps_loc.getAccuracy() >= net_loc.getAccuracy())
                            finalLoc = gps_loc;
                        else
                            finalLoc = net_loc;
                    }
                    else {
                        if (gps_loc != null) {
                            finalLoc = net_loc;
                        } else if (net_loc != null) {
                            finalLoc = gps_loc;
                        }
                    }
                    Log.d("geoecho", finalLoc.toString());
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    editor =pref.edit();;
                    editor.putLong("latitude",(long) finalLoc.getLatitude());
                    editor.putLong("longitude",(long) finalLoc.getLongitude());
                    editor.commit();
                    login = pref.getInt("login",1);
                    if(login == 0) {
                        Intent i = new Intent(SplashScreenActivity.this, LandingActivity.class);
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
