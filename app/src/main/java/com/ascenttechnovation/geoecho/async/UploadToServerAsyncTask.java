package com.ascenttechnovation.geoecho.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ascenttechnovation.geoecho.util.AndroidMultiPartEntity;
import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by ADMIN on 27-06-2015.
 */
public class UploadToServerAsyncTask extends AsyncTask<Void,Void,Boolean>
{
    Context context;
    public UploadToServerCallback listener;
    long totalSize = 0;
    String status,imagePath;
    File sourceFile;

    public interface UploadToServerCallback
    {
        public void onStart(boolean a);
        public void onResult(boolean b);

    }

    public UploadToServerAsyncTask(String imagePath,Context context, UploadToServerCallback listener)
    {
        this.context = context;
        this.listener = listener;
        this.imagePath = imagePath;
        Log.d(Constants.LOG_TAG, " UploadProfileImageAsyncTask " );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart(true);

    }

    @Override
    protected Boolean doInBackground(Void... voids) {



        try
        {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.IMAGE_UPLOAD_URL);
            Log.d(Constants.LOG_TAG,"url: "+Constants.IMAGE_UPLOAD_URL);
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num)
                        {

                        }
                    });
            if(imagePath!=null)
            {
                sourceFile = new File(imagePath);
            }
            else
            {
                sourceFile=new File("NO DATA");
            }
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200)
            {
                // Server response
                responseString = EntityUtils.toString(r_entity);
                Log.d(Constants.LOG_TAG, "RESPONSE STRING " + responseString);
                try
                {
                    JSONObject jsonObject = new JSONObject(responseString);
                    status = jsonObject.getString("statusMessage");
                    JSONArray jsonArray = jsonObject.getJSONArray("photoId");
                    JSONObject jObject = jsonArray.getJSONObject(0);
                    String res = jObject.getString("updatePhoto");
                }
                catch(Exception e)
                {
                    // do something
                }
                return true;
            }
            else
            {
                responseString = "Error occurred! Http Status Code: "+ statusCode;
                Log.d(Constants.LOG_TAG, "responseString " + responseString);
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        listener.onResult(result);

    }

}