package com.ascenttechnovation.geoecho;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by ADMIN on 20-06-2015.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("geoecho", "LoginActivity");
        final EditText mobileno = (EditText) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String jsonstr=readexternaljason();
                try {
                    JSONObject userObject = new JSONObject(jsonstr);
                    JSONObject jsonid = userObject.getJSONObject("lid");
                    String lid = jsonid.getString("lid");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("lid", lid);
                    editor.commit();
                    if(lid!="0") {
                        Intent intent1 = new Intent(LoginActivity.this, LandingActivity.class);
                        startActivity(intent1);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Cannot establized a connection.",Toast.LENGTH_SHORT).show();
                        mobileno.setText("");
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }

        });
    }
    public String readexternaljason() {
        EditText mobileno = (EditText) findViewById(R.id.textView);
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
}
