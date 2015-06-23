package com.ascenttechnovation.geoecho;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class LandingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Log.d("geoecho","LandingActivity");
        ImageView image=(ImageView)findViewById(R.id.image);
        image.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(LandingActivity.this, DetailActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

}
