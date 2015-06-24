package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * Created by ADMIN on 20-06-2015.
 */
public class LoginActivity extends Activity {

    EditText mobileNumber;
    Button login;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String url = "http://www.andealr.com/crontest/geoecho/licenseID.php?contact_no=";

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

        String finalUrl = url+ URLEncoder.encode(mobileNumber.getText().toString(),"utf-8");
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