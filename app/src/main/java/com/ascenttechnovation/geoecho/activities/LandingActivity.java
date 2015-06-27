package com.ascenttechnovation.geoecho.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.util.Constants;


public class LandingActivity extends ActionBarActivity {

    ImageView image;
    Intent i ;
    double latitude,longitude;
    TextView morningReport,afternoonReport,eveningReport;
    int counter =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Log.d(Constants.LOG_TAG,Constants.LandingActivity);

        i = getIntent();
        latitude = i.getDoubleExtra("latitude", 0.0d);
        longitude = i.getDoubleExtra("longitude",0.0d);
        Log.d("SAGAR","landing "+ latitude);
        Log.d("SAGAR","landing "+ longitude);

        findViews();

        setViews();

    }

    private void findViews(){

//        image =(ImageView)findViewById(R.id.image);

        morningReport = (TextView) findViewById(R.id.morning_text_landing_activity);
        afternoonReport = (TextView) findViewById(R.id.afternoon_text_landing_activity);
        eveningReport = (TextView) findViewById(R.id.evening_text_landing_activity);

    }

    private void setViews(){

//        image.setOnClickListener(listener);
        morningReport.setOnClickListener(listener);
        afternoonReport.setOnClickListener(listener);
        eveningReport.setOnClickListener(listener);

    }

    public void enterDetails(){

        Intent intent = new Intent(LandingActivity.this, DetailActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        counter++;
        if(counter%2==0){
            counter=0;
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Press Back Again to Exit",3000).show();
        }

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.morning_text_landing_activity: enterDetails();
                    break;
                case R.id.afternoon_text_landing_activity: enterDetails();
                    break;
                case R.id.evening_text_landing_activity: enterDetails();
                    break;
                default:
                    break;
            }

        }
    };
}
