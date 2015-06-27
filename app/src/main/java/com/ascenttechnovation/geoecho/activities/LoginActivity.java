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

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by ADMIN on 20-06-2015.
 */
public class LoginActivity extends Activity {

    String contactNo,finalUrl;
    EditText mobileNumber;
    Button login;
    ProgressDialog progressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String url = "http://andealr.com/crontest/geoecho/licenseID.php?contact_no=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Constants.LOG_TAG, Constants.LoginActivity);
        setContentView(R.layout.activity_login);

        findViews();

        setViews();


    }

    private void findViews(){

        mobileNumber = (EditText) findViewById(R.id.comment_edit_details_activity);
        login = (Button) findViewById(R.id.login_button_login_activity);
    }

    private void setViews(){

        login.setOnClickListener(listener);


    }

    private void login() throws IOException{

        contactNo = mobileNumber.getText().toString();
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

                    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("contactNo",contactNo);
                    editor.putInt("login",0);
                    editor.commit();
                    Intent i = new Intent(LoginActivity.this,LandingActivity.class);
                    startActivity(i);

                }
                else{

                    Toast.makeText(getApplicationContext(),"There has been a problem.\nTry Again Later",Toast.LENGTH_LONG).show();

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