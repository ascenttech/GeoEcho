package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ADMIN on 22-06-2015.
 */
public class DetailActivity extends Activity{

    private String[] state= {"Select State","Andra Pradesh","Assam","Bihar","Haryana","H P", "J and K","Karnataka", "Kerala","Maharastra"};
    private String[] country= {"Select Country","China","India","Italy","London","Zim"};
    private String[] food= {"Select Food","Veg","Non Veg","Jain"};
    String filePath;

    Uri output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.d("geoecho", "DetailActivity");
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String jsonstr=readexternaljason();
                try {
                    JSONObject userObject = new JSONObject(jsonstr);
                    JSONObject jsonid = userObject.getJSONObject("success");
                    String success = jsonid.getString("success");
                    if(success=="1") {
                        Intent intent = new Intent(DetailActivity.this, LandingActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Cannot establized a connection.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
        });

        Spinner sp1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  R.layout.row_spinner_item, state);
        sp1.setAdapter(adapter_state);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, country);
        sp2.setAdapter(adapter_state1);
        Spinner sp3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, food);
        sp3.setAdapter(adapter_state2);


        final ImageButton call = (ImageButton) findViewById(R.id.camera);
        filePath = Environment.getExternalStorageDirectory() + "/img1.jpeg";
        File file = new File(filePath);
        output = Uri.fromFile(file);

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
            }
        });
    }
    public String readexternaljason() {
        EditText mobileno = (EditText) findViewById(R.id.mobile_number_edit_login_activity);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://192.168.0.101:8080/nilesh/agency/licenseId.php?contactNo="+mobileno.getText().toString());
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    Log.d("geoecho",builder.toString());
                }
            } else {
                Toast.makeText(getApplicationContext(),"No Jason file found.",Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        final ImageButton call = (ImageButton) findViewById(R.id.camera);
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        call.setImageBitmap(bp);

    }
}
