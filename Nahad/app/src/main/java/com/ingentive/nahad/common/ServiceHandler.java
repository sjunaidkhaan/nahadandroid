package com.ingentive.nahad.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by PC on 10-05-2016.
 */
public class ServiceHandler {


    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    DefaultHttpClient httpClient = new DefaultHttpClient();
    HttpEntity httpEntity = null;
    HttpResponse httpResponse = null;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        try {
            // http client


            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }





















//    Context mContext;
//    String serverURL ;
//
//    public ServiceHandler(Context context,String url) {
//        this.mContext = context;
//        this.serverURL=url;
//
//        // Use AsyncTask execute Method To Prevent ANR Problem
//        //new LongOperation().execute(serverURL);
//        LongOperation obj =new LongOperation(serverURL);
//        obj.execute();
//    }
//
//    private class LongOperation extends AsyncTask<String, Void, Void> {
//
//        // Required initialization
//        String serverURL;
//
//        public LongOperation(String url) {
//            this.serverURL=url;
//        }
//
//        private final HttpClient Client = new DefaultHttpClient();
//        private String Content;
//        private String Error = null;
//        private ProgressDialog Dialog = new ProgressDialog(mContext);
//
//
//        protected void onPreExecute() {
//            Dialog.setMessage("Please wait..");
//            Dialog.show();
//        }
//
//        // Call after onPreExecute method
//        protected Void doInBackground(String... urls) {
//            try {
//                // http client
//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpEntity httpEntity = null;
//                HttpResponse httpResponse = null;
//                    HttpGet httpGet = new HttpGet(serverURL);
//
//                    httpResponse = httpClient.execute(httpGet);
//
//                httpEntity = httpResponse.getEntity();
//               String response = EntityUtils.toString(httpEntity);
//
////                JSONObject jsonObj = new JSONObject(response);
////
////                // Getting JSON Array node
////                contacts = jsonObj.getJSONArray(TAG_CONTACTS);
//
//                JSONArray jsonArray = new JSONArray(response);
//
//                //jsonArray = (JSONArray) httpClient.execute(httpGet);
//                Log.d("","");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        protected void onPostExecute(Void unused) {
//            Dialog.dismiss();
//        }
//    }
}
