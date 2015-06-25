package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.async.CheckLoginValidityAsyncTask;
import com.ascenttechnovation.geoecho.util.AlarmReciever;
import com.ascenttechnovation.geoecho.util.Constants;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by ADMIN on 20-06-2015.
 */
public class LoginActivity extends Activity {

    String contactNo,finalUrl;
    EditText mobileNumber;
    Button login;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String url = "http://www.andealr.com/crontest/geoecho/licenseID.php?contact_no=";
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.LOG_TAG, Constants.LoginActivity);
        setContentView(R.layout.activity_login);

        findViews();

        setViews();


    }

    private void findViews(){

        mobileNumber = (EditText) findViewById(R.id.mobile_number_edit_login_activity);
        login = (Button) findViewById(R.id.login_button_login_activity);
    }

    private void setViews(){

        login.setOnClickListener(listener);


    }

    private void login() throws IOException{

        contactNo = mobileNumber.getText().toString();
        sharedPreferences = getPreferences(0);
        editor = sharedPreferences.edit();
        editor.putString("contactNo",contactNo);
        editor.commit();
        finalUrl = url+ URLEncoder.encode(contactNo,"utf-8");
        new CheckLoginValidityAsyncTask(getApplicationContext(),new CheckLoginValidityAsyncTask.CheckLoginValidityListener() {
            @Override
            public void onStart(boolean status) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("GeoEcho");
                progressDialog.setMessage("Loading,Please Wait...");
                progressDialog.show();

            }
            @Override
            public void onResult(boolean result) {

                progressDialog.dismiss();
                if(result){

                    // This is set an alarm to be fired 20 hours from the time he logs in
                    alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getApplicationContext(), AlarmReciever.class);
                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 00);

                    // fire every 20 hours
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            1000 * 60 * 1200, pendingIntent);

                    // start the new actvity

                    Intent i = new Intent(LoginActivity.this,LandingActivity.class);
                    startActivity(i);

                }
                else{

                    Toast.makeText(getApplicationContext(),"There has been a problem.\nTry Again Later",5000).show();

                }

            }
        }).execute(finalUrl);

    }

    @Override
    public void onBackPressed() {

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.login_button_login_activity:
                    try {
                        login();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };

}