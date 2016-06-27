package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.StoriesList;
import com.ingentive.presidentsinfo.activeandroid.StoryInfo;
import com.ingentive.presidentsinfo.common.NetworkChangeReceiver;
import com.ingentive.presidentsinfo.common.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class SettingsActivity extends Activity implements View.OnClickListener {

    private Button btnSmall, btnMedium, btnLarge, btnOn, btnOff, btnBackToStory;
    private int storyId = 0;
    private int conn = 0;
    private StoryInfo storyInfo;
    private ProgressDialog mProgressDialog;
    private String urlStoryInfo = "http://yourbrand.pk/presidentrevealed/services/story_info";
    private String folder_main = "Presidents_Stories";
    private String from = "";
    private int presidentId = 0;
    public static int textSize = 18;
    SharedPreferences sharedpreferences;
    private String MyPREFERENCES = "MyPrefs";
    private SharedPreferences.Editor editor;
    private String font, randomize;
    public static boolean mRandomize=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialize();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            storyId = extras.getInt("story_id");
            from = extras.getString("from");
            presidentId = extras.getInt("president_id");
        }

    }

    private void initialize() {
        mProgressDialog = new ProgressDialog(SettingsActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

        btnSmall = (Button) findViewById(R.id.btn_small);
        btnMedium = (Button) findViewById(R.id.btn_medium);
        btnLarge = (Button) findViewById(R.id.btn_large);
        btnOn = (Button) findViewById(R.id.btn_on);
        btnOff = (Button) findViewById(R.id.btn_off);
        btnBackToStory = (Button) findViewById(R.id.btn_back_to_story);

        btnSmall.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnLarge.setOnClickListener(this);
        btnOn.setOnClickListener(this);
        btnOff.setOnClickListener(this);
        btnBackToStory.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        font = sharedpreferences.getString("text_size", null);
        randomize = sharedpreferences.getString("randomize", null);
        if(font!=null){
            textSize=Integer.parseInt(font);
        }
        if(randomize!=null && randomize.equals("on")){
            mRandomize=true;
        }else if(randomize!=null && randomize.equals("off")){
            mRandomize=false;
        }
        if(textSize==14){
            btnSmall.setSelected(true);
            btnMedium.setSelected(false);
            btnLarge.setSelected(false);
        }else if(textSize==18){
            btnSmall.setSelected(false);
            btnMedium.setSelected(true);
            btnLarge.setSelected(false);
        }else if(textSize==22){
            btnSmall.setSelected(false);
            btnMedium.setSelected(false);
            btnLarge.setSelected(true);
        }
        if(mRandomize==true){
            btnOn.setSelected(true);
            btnOff.setSelected(false);
        }else {
            btnOn.setSelected(false);
            btnOff.setSelected(true);
        }
    }

    private void showpDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hidepDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        TextView textview = (TextView) findViewById(R.id.tv_settings);
        Typeface font;
        switch (v.getId()) {
            case R.id.btn_small:
                textSize = 14;
                btnSmall.setSelected(true);
                btnMedium.setSelected(false);
                btnLarge.setSelected(false);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("text_size", textSize + "");
                editor.commit();
                break;
            case R.id.btn_medium:
                textSize = 18;
                btnSmall.setSelected(false);
                btnMedium.setSelected(true);
                btnLarge.setSelected(false);

                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("text_size", textSize + "");
                editor.commit();
                break;
            case R.id.btn_large:
                textSize = 22;
                btnSmall.setSelected(false);
                btnMedium.setSelected(false);
                btnLarge.setSelected(true);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("text_size", textSize + "");
                editor.commit();
                break;
            case R.id.btn_on:
                btnOn.setSelected(true);
                btnOff.setSelected(false);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("randomize", "on");
                editor.commit();
                break;
            case R.id.btn_off:
                btnOn.setSelected(false);
                btnOff.setSelected(true);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putString("randomize", "off");
                editor.commit();
                break;
            case R.id.btn_back_to_story:
                if (storyId > 0) {
                    StoriesList storiesModel = new StoriesList();
                    storiesModel = new Select().from(StoriesList.class).where("story_id=?", storyId).executeSingle();
                    storyInfo = new StoryInfo();
                    storyInfo = new Select().from(StoryInfo.class).where("story_id=?", storyId).executeSingle();
                    if (storyInfo == null) {
                        conn = NetworkChangeReceiver.getConnectivityStatus(SettingsActivity.this);
                        if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                            new getStoryInfo(storyId, 0).execute();
                        } else {
                            Toast.makeText(SettingsActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                        }
                    } else if (storiesModel != null && storyInfo.getTimeStamp() < storiesModel.getTimeStamp()) {
                        conn = NetworkChangeReceiver.getConnectivityStatus(SettingsActivity.this);
                        if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                            new getStoryInfo(storyId, storyInfo.getTimeStamp()).execute();
                        } else {
                            intent = new Intent(SettingsActivity.this, ReadStoryActivity.class);
                            intent.putExtra("story_id", storyId);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        intent = new Intent(SettingsActivity.this, ReadStoryActivity.class);
                        intent.putExtra("story_id", storyId);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (from.equals("president")) {
                        Intent i = new Intent(SettingsActivity.this, PresidentFactsActivity.class);
                        i.putExtra("president_id", presidentId);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SettingsActivity.this, ReadStoryActivity.class);
                        i.putExtra("story_id", storyId);
                        startActivity(i);
                        finish();
                    }
                }
                break;
        }
    }

    private class getStoryInfo extends AsyncTask<Void, Void, Void> {
        int storyId = 0;
        int timeStamp = 0;
        StoryInfo storyInfoUpdate;

        public getStoryInfo(int storyId, int timeStamp) {
            this.storyId = storyId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("story_id", storyId + ""));
            params.add(new BasicNameValuePair("timestamp", timeStamp + ""));

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlStoryInfo, ServiceHandler.POST, params);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    // {"STATUS":"NOT FOUND"}
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject statusObj = jsonObj.getJSONObject("Success");
                    String code = statusObj.getString("code");
                    String content = statusObj.getString("content");
                    String error = jsonObj.getString("Error");

                    if (code.equals("200")) {
                        JSONObject data = jsonObj.getJSONObject("DATA");
                        int sId = Integer.parseInt(data.getString("id"));
                        int presId = Integer.parseInt(data.getString("president_id"));
                        String sTitle = data.getString("title").toUpperCase();
                        String descriptionHeading = data.getString("description_heading");
                        String shortDescription = data.getString("short_description");
                        String long_description = data.getString("long_description");
                        String background_image = data.getString("background_image");
                        String audiofile = data.getString("audiofile");
                        String audiofile_url = data.getString("audiofile_url");
                        int timeStamp = Integer.parseInt(data.getString("timestamp"));
                        storyInfoUpdate = new StoryInfo();
                        storyInfoUpdate = new Select().from(StoryInfo.class).where("story_id=?", storyId).executeSingle();
                        if (storyInfoUpdate == null) {
                            storyInfo = new StoryInfo();
                            storyInfo.storyId = sId;
                            storyInfo.presidentId = presId;
                            storyInfo.storyTitle = sTitle;
                            storyInfo.descriptionHeading = descriptionHeading;
                            storyInfo.shortDescription = shortDescription;
                            storyInfo.longDescription = long_description;
                            storyInfo.backgroundImage = background_image;
                            storyInfo.storyAudioName = audiofile;
                            storyInfo.storyAudioUrl = audiofile_url;
                            storyInfo.timeStamp = timeStamp;
                        } else if (storyInfo.getTimeStamp() < timeStamp) {
                            storyInfoUpdate.storyId = sId;
                            storyInfoUpdate.presidentId = presId;
                            storyInfoUpdate.storyTitle = sTitle;
                            storyInfoUpdate.descriptionHeading = descriptionHeading;
                            storyInfoUpdate.shortDescription = shortDescription;
                            storyInfoUpdate.longDescription = long_description;
                            storyInfoUpdate.backgroundImage = background_image;
                            storyInfoUpdate.storyAudioName = audiofile;
                            storyInfoUpdate.storyAudioUrl = audiofile_url;
                            storyInfoUpdate.timeStamp = timeStamp;
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hidepDialog();
                }
            } else {
                hidepDialog();
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (storyInfo != null) {
                new DownloadFile(storyInfo).execute();
            } else if (storyInfoUpdate != null) {
                new DownloadFile(storyInfoUpdate).execute();
            } else {
                hidepDialog();
                StoryInfo sinfo = new StoryInfo();
                sinfo = new Select().from(StoryInfo.class).where("story_id=?", storyInfo.getStoryId()).executeSingle();
                if (sinfo != null) {
                    Intent intent = new Intent(SettingsActivity.this, ReadStoryActivity.class);
                    intent.putExtra("story_id", sinfo.getStoryId());
                    startActivity(intent);
                }
            }
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {
        StoryInfo info;

        public DownloadFile(StoryInfo info) {
            this.info = info;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL url_ = new URL(info.getStoryAudioUrl());
                URLConnection conexion = url_.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                InputStream input = new BufferedInputStream(url_.openStream());
                OutputStream output = new FileOutputStream("/sdcard/" + folder_main + "/" + info.getStoryAudioName());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                info.save();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            hidepDialog();
            storyInfo = new StoryInfo();
            storyInfo = new Select().from(StoryInfo.class).where("story_id=?", info.getStoryId()).executeSingle();
            if (storyInfo != null) {
                Intent intent = new Intent(SettingsActivity.this, ReadStoryActivity.class);
                intent.putExtra("story_id", storyInfo.getStoryId());
                startActivity(intent);
            }
        }
    }
}
