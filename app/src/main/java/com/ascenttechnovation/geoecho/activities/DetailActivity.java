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
import com.ascenttechnovation.geoecho.async.UploadToServerAsyncTask;
import com.ascenttechnovation.geoecho.fragment.DatePickerFragment;
import com.ascenttechnovation.geoecho.util.Constants;

import java.io.File;
import java.io.IOException;
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
    String contactno,filePath,state,url="http://andealr.com/crontest/geoecho/dataInsert.php?contact_no=";
    ProgressDialog progressDialog, progresDialog;
    EditText name;
    RadioGroup gender;
    RadioButton check_gender_radiobutton_details_activity;
    Spinner states;
    Button dbutton,button;
    SharedPreferences sharedPreference;
    ImageView image;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100,MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static Uri fileUri,output;
    Bitmap bitmap;
    String finalUrl;
    double latitude,longitude;
    Button dateButton;
    int selectedId;
    DatePickerFragment date;
    Calendar calender;
    Bundle args;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Log.d(Constants.LOG_TAG, Constants.DetailActivity);

        Intent i = getIntent();
        latitude = i.getDoubleExtra("latitude",0.0d);
        longitude = i.getDoubleExtra("longitude",0.0d);

        findViews();

        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,R.layout.row_spinner_item, state_array_details_activity);
        states.setAdapter(adapter_state);

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
        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void findViews(){

        dateButton = (Button) findViewById(R.id.date_button_details_actvity);
        name = (EditText) findViewById(R.id.name_edit_details_activity);
        image = (ImageView) findViewById(R.id.camera_image_details_activity);
        gender = (RadioGroup) findViewById(R.id.gender_radiogroup_details_activity);
        states = (Spinner) findViewById(R.id.state_spinner_details_activity);


    }


    private void showDatePicker() {
        date = new DatePickerFragment();
        calender = Calendar.getInstance();
        args = new Bundle();
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
        sharedPreference = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        selectedId = gender.getCheckedRadioButtonId();
        check_gender_radiobutton_details_activity = (RadioButton) findViewById(selectedId);
        contactno = sharedPreference.getString("contactNo","0");


            new UploadToServerAsyncTask(fileUri.getPath(),getApplicationContext(),new UploadToServerAsyncTask.UploadToServerCallback() {
                @Override
                public void onStart(boolean a) {
                    progresDialog = new ProgressDialog(DetailActivity.this);
                    progresDialog.setTitle("GeoEcho");
                    progresDialog.setMessage("Uploading Image, Please wait...");
                    progresDialog.show();
                }
                @Override
                public void onResult(boolean b) {
                    progresDialog.dismiss();

                    try {
                        finalUrl = url + URLEncoder.encode(contactno, "utf-8")
                                + "&photo_id=" + URLEncoder.encode(Constants.photoId, "utf-8")
                                + "&image_link=" + URLEncoder.encode("photopath", "utf-8")
                                + "&name=" + URLEncoder.encode(name.getText().toString(), "utf-8")
                                + "&gender=" + URLEncoder.encode(check_gender_radiobutton_details_activity.getText().toString(), "utf-8")
                                + "&state=" + URLEncoder.encode(state, "utf-8")
                                + "&date=" + URLEncoder.encode(dbutton.getText().toString(), "utf-8")
                                + "&latitude=" + URLEncoder.encode("" + latitude, "utf-8")
                                + "&longitude=" + URLEncoder.encode("" + longitude, "utf-8");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    new SubmitDetailsAsyncTask(getApplicationContext(),new SubmitDetailsAsyncTask.SubmitDetailsListener() {
                        @Override
                        public void onStart(boolean status) {
                            progressDialog = new ProgressDialog(DetailActivity.this);
                            progressDialog.setTitle("GeoEcho");
                            progressDialog.setMessage("Uploading Data,Please Wait...");
                            progressDialog.show();
                        }
                        @Override
                        public void onResult(boolean result) {
                            progressDialog.dismiss();
                            if(result){
                                Constants.photoId =null;

                                Toast.makeText(getApplicationContext(),"Data submitted sucessfully",3000).show();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent i = new Intent(DetailActivity.this,LandingActivity.class);
                                        startActivity(i);

                                    }
                                },4000);
                            }
                            else{
                                Constants.photoId =null;
                                Toast.makeText(getApplicationContext(),"There has been a problem.\nTry Again Later", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute(finalUrl);


                }
            }).execute();


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
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
            image.setImageBitmap(bitmap);

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
