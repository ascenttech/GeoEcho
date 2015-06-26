package com.ascenttechnovation.geoecho.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by ADMIN on 25-06-2015.
 */
public class AlarmReciever extends BroadcastReceiver {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    public void onReceive(Context context, Intent intent) {

              // WRITE THE CODE FOR CLEARING THE SHARED PREFERENCE
        pref = context.getSharedPreferences("myPref",0);
        editor=pref.edit();
        editor.remove("contactNo");
        editor.remove("login");
        editor.clear();
        editor.commit();
            // cleared the shared preference


    }
}
