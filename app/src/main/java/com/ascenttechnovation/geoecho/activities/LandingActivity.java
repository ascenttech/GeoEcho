package com.ascenttechnovation.geoecho.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.util.Constants;


public class LandingActivity extends ActionBarActivity {

    ImageView image;
    Intent i ;
    double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Log.d(Constants.LOG_TAG,Constants.LandingActivity);

        i = getIntent();
        latitude = i.getDoubleExtra("latitude",0.0d);
        longitude = i.getDoubleExtra("longitude",0.0d);
        Log.d("SAGAR","landing "+ latitude);
        Log.d("SAGAR","landing "+ longitude);

        findViews();

        setViews();

    }

    private void findViews(){

        image =(ImageView)findViewById(R.id.image);
    }

    private void setViews(){

        image.setOnClickListener(listener);
    }

    public void selectImage(){

        Intent intent = new Intent(LandingActivity.this, DetailActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.image: selectImage();
                    break;
                default:
                    break;
            }

        }
    };
}
