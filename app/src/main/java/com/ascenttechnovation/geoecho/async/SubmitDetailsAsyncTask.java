package com.ascenttechnovation.geoecho.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ascenttechnovation.geoecho.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ADMIN on 25-06-2015.
 */
public class SubmitDetailsAsyncTask extends AsyncTask<String,Void,Boolean> {

    Context context;
    SubmitDetailsListener listener;
    HttpEntity httpEntity;
    HttpPost httpPost;
    HttpClient httpClient;
    HttpResponse httpResponse;
    String responseString;

    public interface SubmitDetailsListener{

        public void onStart(boolean status);
        public void onResult(boolean result);
    }

    public SubmitDetailsAsyncTask(Context context, SubmitDetailsListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onStart(true);
    }

    @Override
    protected Boolean doInBackground(String... url) {

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
