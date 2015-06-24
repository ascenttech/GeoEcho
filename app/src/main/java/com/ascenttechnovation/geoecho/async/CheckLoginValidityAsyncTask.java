package com.ascenttechnovation.geoecho.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ADMIN on 24-06-2015.
 */
public class CheckLoginValidityAsyncTask extends AsyncTask<String,Void,Boolean> {

    Context context;
    CheckLoginValidityListener listener;
    HttpEntity httpEntity;
    HttpPost httpPost;
    HttpClient httpClient;
    HttpResponse httpResponse;
    String responseString;

    public interface CheckLoginValidityListener{

        public void onStart(boolean status);
        public void onResult(boolean result);
    }

    public CheckLoginValidityAsyncTask(Context context, CheckLoginValidityListener listener) {
        this.context = context;
        this.listener = listener;
        Log.d(Constants.LOG_TAG,Constants.CheckLoginValidityAsyncTask);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart(true);
    }

    @Override
    protected Boolean doInBackground(String... url) {

        Log.d(Constants.LOG_TAG,Constants.CheckLoginValidityAsyncTask);
        Log.d(Constants.LOG_TAG," The url to be fetched "+url[0]);
        try{

            httpPost = new HttpPost(url[0]);
            httpClient = new DefaultHttpClient();
            httpResponse = httpClient.execute(httpPost);

            int status = httpResponse.getStatusLine().getStatusCode();

            if(status == 200){

                httpEntity = httpResponse.getEntity();
                responseString = EntityUtils.toString(httpEntity);


                JSONObject jsonObject = new JSONObject(responseString);
                Log.d(Constants.LOG_TAG," JSON OBJECT "+ jsonObject);

                JSONArray jsonArray = jsonObject.getJSONArray("listing");
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject nestedJsonObject = jsonArray.getJSONObject(i);
                    String licenseId = nestedJsonObject.getString("license_id");
                }

                return true;

            }
            else{

                Log.d(Constants.LOG_TAG," There was an error with status code" + status);
                return false;
            }

        }
        catch(Exception e){

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
