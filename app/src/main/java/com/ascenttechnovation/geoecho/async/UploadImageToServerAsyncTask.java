package com.ascenttechnovation.geoecho.async;

import android.content.Context;
import android.os.AsyncTask;

import com.ascenttechnovation.geoecho.activities.CameraActivity;
import com.ascenttechnovation.geoecho.util.AndroidMultiPartEntity;
import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by ADMIN on 25-06-2015.
 */
public class UploadImageToServerAsyncTask extends AsyncTask<Void, Integer, String>{

    long totalSize=0;
    String status;
    public int photoId;
    File sourceFile;
    Context context;


    /**
     * Implement this somewhere to get the result
     */
    public interface UploadImageToServerCallback {

        public void onStart(boolean a);
        public void onResult(String b);

    }

    private UploadImageToServerCallback listener;


    public UploadImageToServerAsyncTask(Context context, UploadImageToServerCallback listener) {
        this.context = context;
        this.listener = listener; // save callback

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart(true);
    }


    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.IMAGE_UPLOAD_URL);
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            //sourceFile = new File(CameraActivity.imagepath);

            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);

                try {
                    JSONObject jsonObject = new JSONObject(responseString);
//
//                    status = jsonObject.getString("statusMessage");
//
//                    JSONArray jsonArray = jsonObject.getJSONArray("photo");
//
//                    JSONObject jObject = jsonArray.getJSONObject(0);
//                    photoId = jObject.getInt("photoID");


                } catch (Exception e) {
                    // do something
                }


            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onResult(result);
    }
}