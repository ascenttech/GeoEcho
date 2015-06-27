package com.ascenttechnovation.geoecho.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ascenttechnovation.geoecho.R;
import com.ascenttechnovation.geoecho.async.SubmitDetailsAsyncTask;
import com.ascenttechnovation.geoecho.async.UploadImageToServerAsyncTask;
import com.ascenttechnovation.geoecho.fragment.DatePickerFragment;
import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ADMIN on 22-06-2015.
 */
public class DetailActivity extends FragmentActivity {

    private String[] state_array_details_activity = {"Select State","Andra Pradesh","Assam","Bihar","Haryana","H P", "J and K","Karnataka", "Kerala","Maharastra"};
    String contactno,filePath,date,name,state,gender,url="http://andealr.com/crontest/geoecho/dataInsert.php?contact_no=";
    long latitude,longitude;
    ProgressDialog submit_progressDialog_details_activity, upload_progressDialog_details_activity;
    EditText name_edit_details_activity;
    RadioGroup gender_radioGroup_details_activity;
    RadioButton check_gender_radiobutton_details_activity;
    Spinner state_spinner_details_activity;
    Button dbutton,button;
    SharedPreferences insert_sharedpreference_details_activity;
    ImageView image;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100,MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static Uri fileUri,output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.d(Constants.LOG_TAG, Constants.DetailActivity);
        Button button1 = (Button) findViewById(R.id.date_button_details_actvity);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePicker();
            }
        });
        name_edit_details_activity = (EditText) findViewById(R.id.name_edit_details_activity);
        gender_radioGroup_details_activity = (RadioGroup) findViewById(R.id.gender_radiogroup_details_activity);
        state_spinner_details_activity = (Spinner) findViewById(R.id.state_spinner_details_activity);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, state_array_details_activity);
        state_spinner_details_activity.setAdapter(adapter_state);
        dbutton = (Button) findViewById(R.id.date_button_details_actvity);
        button = (Button) findViewById(R.id.submit_button_details_activity);
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
        state_spinner_details_activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View seletedItem, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                state = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final ImageButton call = (ImageButton) findViewById(R.id.camera_image_details_activity);
        filePath = Environment.getExternalStorageDirectory() + "/img1.jpeg";
        File file = new File(filePath);
        output = Uri.fromFile(file);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                uploadImage();
            }
        });
    }
    public String readexternaljason() {
        EditText mobileno = (EditText) findViewById(R.id.comment_edit_login_activity);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://192.168.0.104/nilesh/dataInsert.php?contactNo="+mobileno.getText().toString());
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
                Toast.makeText(getApplicationContext(),"No Json file found.",Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = String.valueOf(year)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(dayOfMonth);
            dbutton.setText(date);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void login() throws IOException{
        insert_sharedpreference_details_activity = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int selectedId = gender_radioGroup_details_activity.getCheckedRadioButtonId();
        check_gender_radiobutton_details_activity = (RadioButton) findViewById(selectedId);
        name = name_edit_details_activity.getText().toString();
        gender = check_gender_radiobutton_details_activity.getText().toString();
        latitude = insert_sharedpreference_details_activity.getLong("latitude", 0);
        longitude = insert_sharedpreference_details_activity.getLong("longitude", 0);
        contactno = insert_sharedpreference_details_activity.getString("contactNo","0");

        String finalUrl = url + URLEncoder.encode(contactno, "utf-8")
                + "&photo_id=" + URLEncoder.encode("1", "utf-8")
                + "&image_link=" + URLEncoder.encode("photopath", "utf-8")
                + "&name=" + URLEncoder.encode(name, "utf-8")
                + "&gender=" + URLEncoder.encode(gender, "utf-8")
                + "&state=" + URLEncoder.encode(state, "utf-8")
                + "&date=" + URLEncoder.encode(dbutton.getText().toString(), "utf-8")
                + "&latitude=" + URLEncoder.encode(""+latitude, "utf-8")
                + "&longitude=" + URLEncoder.encode(""+longitude, "utf-8");
        new SubmitDetailsAsyncTask(getApplicationContext(),new SubmitDetailsAsyncTask.SubmitDetailsListener() {
            @Override
            public void onStart(boolean status) {
                submit_progressDialog_details_activity = new ProgressDialog(DetailActivity.this);
                submit_progressDialog_details_activity.setTitle("GeoEcho");
                submit_progressDialog_details_activity.setMessage("Sending,Please Wait...");
                submit_progressDialog_details_activity.show();
            }
            @Override
            public void onResult(boolean result) {
                submit_progressDialog_details_activity.dismiss();
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
    //image upload
    public void uploadImage(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    private void previewCapturedImage(){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
            Log.d(Constants.LOG_TAG,bitmap.toString());
            image.setImageBitmap(bitmap);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new UploadImageToServerAsyncTask(getApplicationContext(),new UploadImageToServerAsyncTask.UploadImageToServerCallback() {
                        @Override
                        public void onStart(boolean a) {
                            upload_progressDialog_details_activity = new ProgressDialog(DetailActivity.this);
                            upload_progressDialog_details_activity.setTitle("Uploading please wait");
                            upload_progressDialog_details_activity.setMessage("Loading");
                            upload_progressDialog_details_activity.show();
                        }
                        @Override
                        public void onResult(String b) {
                            upload_progressDialog_details_activity.dismiss();
                        }
                    }).execute();
                }
            },3000);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
}
