package com.ascenttechnovation.geoecho.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.async.CheckLoginValidityAsyncTask;
import com.ascenttechnovation.geoecho.async.SubmitDetailsAsyncTask;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by ADMIN on 22-06-2015.
 */
public class DetailActivity extends FragmentActivity {

    private String[] astate= {"Select State","Andra Pradesh","Assam","Bihar","Haryana","H P", "J and K","Karnataka", "Kerala","Maharastra"};
    String contactno,filePath,date,name,state,gender,url="http://192.168.0.107/nilesh/geoecho/dataInsert.php?contact_no=";
    ProgressDialog progressDialog;
    EditText ed1;
    RadioGroup radioGroup;
    RadioButton rb;
    Spinner spinner;
    Uri output;
    Button dbutton,button;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.d(Constants.LOG_TAG, Constants.DetailActivity);
        Button button1 = (Button) findViewById(R.id.date);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePicker();
            }
        });
        ed1 = (EditText) findViewById(R.id.name);
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,astate);
        spinner.setAdapter(adapter_state);
        dbutton = (Button) findViewById(R.id.date);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    login();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View seletedItem, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                state = item.toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
        HttpGet httpGet = new HttpGet("http://192.168.0.107/nilesh/dataInsert.php?contactNo="+mobileno.getText().toString());
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


    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
            dbutton.setText(date);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void login() throws IOException{

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(selectedId);
        name = ed1.getText().toString();
        gender = rb.getText().toString();
        //contactno = "9820052210";
        contactno = pref.getString("contactNo","0");

        String finalUrl = url + URLEncoder.encode(contactno, "utf-8")
                + "&image_link=" + URLEncoder.encode("drhshsdt", "utf-8")
                + "&name=" + URLEncoder.encode(name, "utf-8")
                + "&gender=" + URLEncoder.encode(gender, "utf-8")
                + "&state=" + URLEncoder.encode(state, "utf-8")
                + "&date=" + URLEncoder.encode(dbutton.getText().toString(), "utf-8");
        new SubmitDetailsAsyncTask(getApplicationContext(),new SubmitDetailsAsyncTask.SubmitDetailsListener() {
            @Override
            public void onStart(boolean status) {

                progressDialog = new ProgressDialog(DetailActivity.this);
                progressDialog.setTitle("GeoEcho");
                progressDialog.setMessage("Sending,Please Wait...");
                progressDialog.show();

            }
            @Override
            public void onResult(boolean result) {

                progressDialog.dismiss();
                if(result){

                    Intent i = new Intent(DetailActivity.this,LandingActivity.class);
                    startActivity(i);

                }
                else{

                    Toast.makeText(getApplicationContext(),"There has been a problem.\nTry Again Later", Toast.LENGTH_LONG).show();

                }

            }
        }).execute(finalUrl);

    }
}
