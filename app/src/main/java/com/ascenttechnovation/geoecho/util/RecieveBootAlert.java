package com.ascenttechnovation.geoecho.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ADMIN on 25-06-2015.
 */
public class RecieveBootAlert extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction()== Intent.ACTION_BOOT_COMPLETED){


        }
    }
}
