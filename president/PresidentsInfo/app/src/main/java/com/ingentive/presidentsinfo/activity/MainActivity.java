package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.PresTSInFirstStory;
import com.ingentive.presidentsinfo.activeandroid.PresidentInfo;
import com.ingentive.presidentsinfo.activeandroid.PresidentsList;
import com.ingentive.presidentsinfo.activeandroid.StoriesList;
import com.ingentive.presidentsinfo.activeandroid.StoryInfo;
import com.ingentive.presidentsinfo.adapter.PresidentsListAdapter;
import com.ingentive.presidentsinfo.adapter.StoriesListAdapter;
import com.ingentive.presidentsinfo.adapter.TextAdapter;
import com.ingentive.presidentsinfo.common.NetworkChangeReceiver;
import com.ingentive.presidentsinfo.common.ServiceHandler;
import com.ingentive.presidentsinfo.model.TextModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnContents, btnPresidential, btnCredits, btnIntro, btnReadStory;
    private ImageButton ibtnHome;
    private RelativeLayout relativeLayout, creditsLayout;
    private ListView listView;
    private TextAdapter mAdapter;
    private PresidentsListAdapter presidentsListAdapter;
    private TextModel model = new TextModel();
    private List<TextModel> list = new ArrayList<TextModel>();
    private ProgressDialog mProgressDialog;
    private int conn = 0;
    private StoriesListAdapter storiesListAdapter;
    public String urlPresidentsList = "http://yourbrand.pk/presidentrevealed/services/presidents_list";
    public String urlStoriesList = "http://yourbrand.pk/presidentrevealed/services/stories_list";
    public String urlFirstStory = "http://yourbrand.pk/presidentrevealed/services/first_story";
    public String urlStoryInfo = "http://yourbrand.pk/presidentrevealed/services/story_info";
    public String urlPresidentInfo = "http://yourbrand.pk/presidentrevealed/services/president_info";
    private List<PresidentsList> presidentsLists;
    private List<StoriesList> storiesLists;
    private StoryInfo storyInfo;
    private PresidentInfo presidentInfo;
    private Select select;
    String dwnload_file_path = "http://vprbbc.streamguys.net/vprbbc24.mp3";
    private ProgressDialog pdialog;
    private String folder_main = "Presidents_Stories";
    private String folder_main_images = "Presidents_Stories/Images";
    //private PresidentsList presidentsListModel;
    SharedPreferences sharedpreferences;
    private String MyPREFERENCES = "MyPrefs";
    private SharedPreferences.Editor editor;
    private int firstStoryId = 0;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File imaages = new File(Environment.getExternalStorageDirectory(), folder_main_images);
        if (!imaages.exists()) {
            imaages.mkdirs();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("btn_clicked").toString().equals("btn_contents")) {
                conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    new getStoriesList().execute();
                } else {
                    showStoriesList();
                }
            }
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initialize() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

        listView = (ListView) findViewById(R.id.listView);
        relativeLayout = (RelativeLayout) findViewById(R.id.top_layout);
        creditsLayout = (RelativeLayout) findViewById(R.id.top_layout_credits);
        btnContents = (Button) findViewById(R.id.btn_contents);
        btnPresidential = (Button) findViewById(R.id.btn_p_f);
        btnCredits = (Button) findViewById(R.id.btn_credits);
        btnIntro = (Button) findViewById(R.id.btn_intro);
        btnReadStory = (Button) findViewById(R.id.btn_read_story);
        ibtnHome = (ImageButton) findViewById(R.id.ibtn_home);
        btnContents.setOnClickListener(this);
        btnPresidential.setOnClickListener(this);
        btnCredits.setOnClickListener(this);
        btnIntro.setOnClickListener(this);
        btnReadStory.setOnClickListener(this);
        ibtnHome.setOnClickListener(this);

        btnContents.setSelected(false);
        btnPresidential.setSelected(false);
        btnCredits.setSelected(false);
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
        switch (v.getId()) {
            case R.id.btn_contents:
                conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    new getStoriesList().execute();
                } else {
                    showStoriesList();
                }
                break;
            case R.id.btn_p_f:
                conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    new getPresidentsList().execute();
                } else {
                    showPresidentsList();
                }
                // showStoriesList();

                break;
            case R.id.btn_credits:
                creditsArray();
                break;
            case R.id.btn_intro:
                intent = new Intent(MainActivity.this, IntroductionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_read_story:
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int id = sharedpreferences.getInt("first_story_id", 0);
                PresTSInFirstStory presTSInFirstStory=new PresTSInFirstStory();
                presTSInFirstStory=new Select().from(PresTSInFirstStory.class).executeSingle();
                conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    new getFirstStory().execute();
                } else if (presTSInFirstStory != null) {
                    Intent inte = new Intent(MainActivity.this, ReadStoryActivity.class);
                    inte.putExtra("story_id", presTSInFirstStory.getsId());
                    startActivity(inte);
                }else {
                    Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                }


//                storyInfo = new Select().from(StoryInfo.class).where("story_id=?", id).executeSingle();
//                if (storyInfo != null) {
//                    StoriesList storiesList = new StoriesList();
//                    storiesList = new Select().from(StoriesList.class).where("story_id=?", storyInfo.getStoryId()).executeSingle();
//                    if (storiesList != null && storiesList.getTimeStamp() < storyInfo.getTimeStamp()) {
//                        conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
//                        if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
//                            new getStoryInfo(storyInfo.getStoryId(), storyInfo.getTimeStamp()).execute();
//                        } else {
//                            Intent inte = new Intent(MainActivity.this, ReadStoryActivity.class);
//                            inte.putExtra("story_id", storyInfo.getStoryId());
//                            startActivity(inte);
//                        }
//                    } else {
//                        Intent inte = new Intent(MainActivity.this, ReadStoryActivity.class);
//                        inte.putExtra("story_id", storyInfo.getStoryId());
//                        startActivity(inte);
//                    }
//                } else {
//                    conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
//                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
//                        new getFirstStory().execute();
//                    } else {
//                        Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
//                    }
//                }

                break;
            case R.id.ibtn_home:
                relativeLayout.setVisibility(View.VISIBLE);
                btnContents.setSelected(false);
                btnPresidential.setSelected(false);
                btnCredits.setSelected(false);
                listView.setVisibility(View.GONE);
                btnIntro.setVisibility(View.VISIBLE);
                btnReadStory.setVisibility(View.VISIBLE);
                relativeLayout.setBackgroundResource(R.drawable.bg_main);
                creditsLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void showPresidentsList() {
        btnContents.setSelected(false);
        btnPresidential.setSelected(true);
        btnCredits.setSelected(false);
        relativeLayout.setVisibility(View.VISIBLE);
        creditsLayout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        btnIntro.setVisibility(View.GONE);
        btnReadStory.setVisibility(View.GONE);
        relativeLayout.setBackgroundResource(R.drawable.bg_contents);
        listView = (ListView) findViewById(R.id.listView);
        presidentsLists = new ArrayList<PresidentsList>();
        presidentsLists = new Select().all().from(PresidentsList.class).execute();

        if (presidentsLists.size() > 0) {
            presidentsListAdapter = new PresidentsListAdapter(getApplication(), presidentsLists, R.layout.custom_row_text);
            listView.setAdapter(presidentsListAdapter);
        } else {
            Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int presId = presidentsLists.get(position).getPresId();
                // 1467189358
                int timeStamp = presidentsLists.get(position).getTimeStamp();
                Log.d("presId: " + presId, "  time: " + timeStamp);
                presidentInfo = new PresidentInfo();
                presidentInfo = new Select().from(PresidentInfo.class).where("president_id=?", presId).executeSingle();
                if (presidentInfo == null) {
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getPresidentInfo(presId, 0).execute();
                    } else {
                        Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                    }
                } else if (presidentInfo.getTimeStamp() < timeStamp) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getPresidentInfo(presId, presidentInfo.getTimeStamp()).execute();
                    } else {
                        Intent intent = new Intent(MainActivity.this, PresidentFactsActivity.class);
                        intent.putExtra("president_id", presId);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, PresidentFactsActivity.class);
                    intent.putExtra("president_id", presId);
                    startActivity(intent);
                }
            }
        });
    }

    private void showStoriesList() {
        relativeLayout.setVisibility(View.VISIBLE);
        creditsLayout.setVisibility(View.GONE);
        btnContents.setSelected(true);
        btnPresidential.setSelected(false);
        btnCredits.setSelected(false);
        listView.setVisibility(View.VISIBLE);
        btnIntro.setVisibility(View.GONE);
        btnReadStory.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);
        relativeLayout.setBackgroundResource(R.drawable.bg_contents);
        storiesLists = new ArrayList<StoriesList>();
        storiesLists = new Select().all().from(StoriesList.class).execute();
        if (storiesLists.size() > 0) {
            storiesListAdapter = new StoriesListAdapter(getApplication(), storiesLists, R.layout.custom_row_text);
            listView.setAdapter(storiesListAdapter);
        } else {
            Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int storyId = storiesLists.get(position).getsId();
                int timeStamp = storiesLists.get(position).getTimeStamp();
                Log.d("storyid: " + storyId, "  time: " + timeStamp);
                storyInfo = new StoryInfo();
                storyInfo = new Select().from(StoryInfo.class).where("story_id=?", storyId).executeSingle();
                if (storyInfo == null) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getStoryInfo(storyId, 0).execute();
                    } else {
                        Toast.makeText(MainActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                    }
                } else if (storyInfo.getTimeStamp() < timeStamp) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getStoryInfo(storyId, storyInfo.getTimeStamp()).execute();
                    } else {
                        Intent intent = new Intent(MainActivity.this, ReadStoryActivity.class);
                        intent.putExtra("story_id", storyId);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, ReadStoryActivity.class);
                    intent.putExtra("story_id", storyId);
                    startActivity(intent);
                }
            }
        });
    }

    private void creditsArray() {
        creditsLayout.setVisibility(View.VISIBLE);
        btnContents.setSelected(false);
        btnPresidential.setSelected(false);
        btnCredits.setSelected(true);
        btnIntro.setVisibility(View.GONE);
        btnReadStory.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class getPresidentsList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlPresidentsList+"/"+System.currentTimeMillis(), ServiceHandler.GET);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //String status = jsonObj.getString("Success");
                    //JSONArray status = jsonObj.getJSONArray("Success");
                    JSONObject statusObj = jsonObj.getJSONObject("Success");
                    String code = statusObj.getString("code");
                    String content = statusObj.getString("content");
                    String error = jsonObj.getString("Error");

                    if (code.equals("200")) {
                        JSONArray data = jsonObj.getJSONArray("DATA");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            int id = Integer.parseInt(c.getString("id"));
                            String presidentName = c.getString("president_name").toUpperCase();
                            int timestamp = Integer.parseInt(c.getString("timestamp"));

                            PresidentsList presidentsListModel = new PresidentsList();
                            presidentsListModel = new Select().from(PresidentsList.class).where("president_id = ?", id).executeSingle();
                            if (presidentsListModel == null) {
                                PresidentsList model = new PresidentsList();
                                model.presId = id;
                                model.presidentName = presidentName;
                                model.timeStamp = timestamp;
                                model.save();
                            } else if (presidentsListModel.getTimeStamp() < timestamp) {
                                presidentsListModel.presId = id;
                                presidentsListModel.presidentName = presidentName;
                                presidentsListModel.timeStamp = timestamp;
                                presidentsListModel.save();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hidepDialog();
            showPresidentsList();
        }
    }

    private class getStoriesList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlStoriesList+"/"+ System.currentTimeMillis(), ServiceHandler.GET);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject statusObj = jsonObj.getJSONObject("Success");
                    String code = statusObj.getString("code");
                    String content = statusObj.getString("content");
                    String error = jsonObj.getString("Error");

                    if (code.equals("200")) {
                        JSONArray data = jsonObj.getJSONArray("DATA");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject c = data.getJSONObject(i);
                            int sId = Integer.parseInt(c.getString("id"));
                            String sTitle = c.getString("title").toUpperCase();
                            int presId = Integer.parseInt(c.getString("president_id"));
                            int timestamp = Integer.parseInt(c.getString("timestamp"));
                            int presidentTimeStamp = Integer.parseInt(c.getString("president_timestamp"));

                            StoriesList storiesList = new StoriesList();
                            storiesList = new Select().from(StoriesList.class).where("story_id = ?", sId).executeSingle();
                            if (storiesList == null) {
                                StoriesList storiesModel = new StoriesList();
                                storiesModel.sId = sId;
                                storiesModel.sTitle = sTitle;
                                storiesModel.presId = presId;
                                storiesModel.timeStamp = timestamp;
                                storiesModel.presidentTimeStamp=presidentTimeStamp;
                                storiesModel.save();

                            } else if (storiesList.getTimeStamp() < timestamp) {
                                storiesList.sId = sId;
                                storiesList.sTitle = sTitle;
                                storiesList.presId = presId;
                                storiesList.timeStamp = timestamp;
                                storiesList.presidentTimeStamp=presidentTimeStamp;
                                storiesList.save();
                            }
                            else if (storiesList.getPresidentTimeStamp() < presidentTimeStamp) {
                                storiesList.sId = sId;
                                storiesList.sTitle = sTitle;
                                storiesList.presId = presId;
                                storiesList.timeStamp = timestamp;
                                storiesList.presidentTimeStamp=presidentTimeStamp;
                                storiesList.save();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hidepDialog();
            showStoriesList();
        }
    }

    private class getFirstStory extends AsyncTask<Void, Void, Void> {
        StoryInfo storyInfoNew;
        StoryInfo storyInfoUpdate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlFirstStory+"/"+System.currentTimeMillis(), ServiceHandler.GET);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject statusObj = jsonObj.getJSONObject("Success");
                    String code = statusObj.getString("code");
                    String content = statusObj.getString("content");
                    String error = jsonObj.getString("Error");
                    if (code.equals("200")) {
                        //if (status.equals("SUCCESS")) {
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
                        int prestTmeStamp = Integer.parseInt(data.getString("president_timestamp"));

//                        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                        editor = sharedpreferences.edit();
//                        editor.putInt("first_story_id", sId);
//                        editor.commit();
                        PresTSInFirstStory firstStory=new PresTSInFirstStory();
                        firstStory=new Select().from(PresTSInFirstStory.class).executeSingle();
                        if(firstStory==null){
                            PresTSInFirstStory story=new PresTSInFirstStory();
                            story.presId=presId;
                            story.sId=sId;
                            story.presidentTimeStamp=prestTmeStamp;
                            story.save();
                        }else {
                            firstStory.presId=presId;
                            firstStory.sId=sId;
                            firstStory.presidentTimeStamp=prestTmeStamp;
                            firstStory.save();
                        }
                        storyInfoUpdate = new StoryInfo();
                        storyInfoUpdate = new Select().from(StoryInfo.class).where("story_id=?", sId).executeSingle();
                        if (storyInfoUpdate == null) {
                            storyInfoNew = new StoryInfo();
                            storyInfoNew.storyId = sId;
                            storyInfoNew.presidentId = presId;
                            storyInfoNew.storyTitle = sTitle;
                            storyInfoNew.descriptionHeading = descriptionHeading;
                            storyInfoNew.shortDescription = shortDescription;
                            storyInfoNew.longDescription = long_description;
                            storyInfoNew.backgroundImage = background_image;
                            storyInfoNew.storyAudioName = audiofile;
                            storyInfoNew.storyAudioUrl = audiofile_url;
                            storyInfoNew.timeStamp = timeStamp;

                            //storyInfoNew.save();
                        } else if (storyInfoUpdate.getTimeStamp() < timeStamp) {
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
                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (storyInfoNew != null) {
                new DownloadFile(storyInfoNew).execute();
            } else if (storyInfoUpdate != null) {
                new DownloadFile(storyInfoUpdate).execute();
            } else {
                hidepDialog();
                firstStory();
            }

        }
    }

    private class getStoryInfo extends AsyncTask<Void, Void, Void> {
        int storyId = 0;
        int timeStamp = 0;
        StoryInfo storyInfoNew;

        public getStoryInfo(int storyId, int timeStamp) {
            this.storyId = storyId;
            this.timeStamp = timeStamp;

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
            String jsonStr = sh.makeServiceCall(urlStoryInfo+"/"+System.currentTimeMillis(), ServiceHandler.POST, params);
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
                        String moral = data.getString("moral");
                        String background_image = data.getString("background_image");
                        String audiofile = data.getString("audiofile");
                        String audiofile_url = data.getString("audiofile_url");
                        int timeStamp = Integer.parseInt(data.getString("timestamp"));


                        storyInfo = new StoryInfo();
                        storyInfo = new Select().from(StoryInfo.class).where("story_id=?", storyId).executeSingle();
                        if (storyInfo == null) {
                            storyInfoNew = new StoryInfo();
                            storyInfoNew.storyId = sId;
                            storyInfoNew.presidentId = presId;
                            storyInfoNew.storyTitle = sTitle;
                            storyInfoNew.descriptionHeading = descriptionHeading;
                            storyInfoNew.shortDescription = shortDescription;
                            storyInfoNew.longDescription = long_description;
                            storyInfoNew.storyMoral = moral;
                            storyInfoNew.backgroundImage = background_image;
                            storyInfoNew.storyAudioName = audiofile;
                            storyInfoNew.storyAudioUrl = audiofile_url;
                            storyInfoNew.timeStamp = timeStamp;
                        } else if (storyInfo.getTimeStamp() < timeStamp) {
                            storyInfo.storyId = sId;
                            storyInfo.presidentId = presId;
                            storyInfo.storyTitle = sTitle;
                            storyInfo.descriptionHeading = descriptionHeading;
                            storyInfo.shortDescription = shortDescription;
                            storyInfo.longDescription = long_description;
                            storyInfo.storyMoral = moral;
                            storyInfo.backgroundImage = background_image;
                            storyInfo.storyAudioName = audiofile;
                            storyInfo.storyAudioUrl = audiofile_url;
                            storyInfo.timeStamp = timeStamp;
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
            } else if (storyInfoNew != null) {
                new DownloadFile(storyInfoNew).execute();
            } else {
                hidepDialog();
                StoryInfo sinfo = new StoryInfo();
                sinfo = new Select().from(StoryInfo.class).where("story_id=?", storyInfo.getStoryId()).executeSingle();
                if (sinfo != null) {
                    Intent intent = new Intent(MainActivity.this, ReadStoryActivity.class);
                    intent.putExtra("story_id", sinfo.getStoryId());
                    startActivity(intent);
                }
            }
        }
    }

    private class getPresidentInfo extends AsyncTask<Void, Void, Void> {
        int presiId = 0;
        int timeStamp = 0;
        PresidentInfo mPresInfo;

        public getPresidentInfo(int presiId, int timeStamp) {
            this.presiId = presiId;
            this.timeStamp = timeStamp;
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
            params.add(new BasicNameValuePair("president_id", presiId + ""));
            params.add(new BasicNameValuePair("timestamp", timeStamp + ""));

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlPresidentInfo+"/"+System.currentTimeMillis(), ServiceHandler.POST, params);
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

                        int presId = Integer.parseInt(data.getString("id"));
                        String presName = data.getString("president_name").toUpperCase();
                        String presImageUrl = data.getString("president_image");
                        String presSignatureImageUrl = data.getString("signature_image");
                        String presQuotation = data.getString("quotation");
                        String backgroundImageUrl = data.getString("background_image");
                        String presFact = data.getString("fact");
                        int timeStamp = Integer.parseInt(data.getString("timestamp"));

                        presidentInfo = new PresidentInfo();
                        presidentInfo = new Select().from(PresidentInfo.class).where("president_id=?", presiId).executeSingle();
                        if (presidentInfo == null) {
                            mPresInfo = new PresidentInfo();
                            mPresInfo.presId = presId;
                            mPresInfo.presName = presName;
                            mPresInfo.presImageUrl = presImageUrl;
                            mPresInfo.presImageName = presId + "_president_" + presName + ".png";
                            mPresInfo.presSignatureImageUrl = presSignatureImageUrl;
                            mPresInfo.presSignatureImageName = presId + "_sign_" + presName + ".png";
                            mPresInfo.presQuotation = presQuotation;
                            mPresInfo.backgroundImageUrl = backgroundImageUrl;
                            mPresInfo.backgroundImageName = presId + "_bg_" + presName + ".png";
                            mPresInfo.presFact = presFact;
                            mPresInfo.timeStamp = timeStamp;
                        } else if (presidentInfo.getTimeStamp() < timeStamp) {
                            presidentInfo.presId = presId;
                            presidentInfo.presName = presName;
                            presidentInfo.presImageUrl = presImageUrl;
                            presidentInfo.presImageName = presId + "_president_" + presName + ".png";
                            presidentInfo.presSignatureImageUrl = presSignatureImageUrl;
                            presidentInfo.presSignatureImageName = presId + "_sign_" + presName + ".png";
                            presidentInfo.presQuotation = presQuotation;
                            presidentInfo.backgroundImageUrl = backgroundImageUrl;
                            presidentInfo.backgroundImageName = presId + "_bg_" + presName + ".png";
                            presidentInfo.presFact = presFact;
                            presidentInfo.timeStamp = timeStamp;
                        }
                        //info.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                android.util.Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (mPresInfo != null) {
                new downloadImage(mPresInfo).execute();
            } else if (presidentInfo != null) {
                new downloadImage(presidentInfo).execute();
            } else {
                hidepDialog();
            }
        }
    }

    private void firstStory() {
        StoryInfo storyInfo = new StoryInfo();
        storyInfo = new Select().from(StoryInfo.class).orderBy("story_id ASC").executeSingle();
        if (storyInfo != null) {
            Intent intent = new Intent(MainActivity.this, ReadStoryActivity.class);
            intent.putExtra("story_id", storyInfo.getStoryId());
            startActivity(intent);
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
               //Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NAHAD_PDF" + File.separator + bookName;
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator  + folder_main + File.separator+ info.getStoryAudioName());

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
                Intent intent = new Intent(MainActivity.this, ReadStoryActivity.class);
                intent.putExtra("story_id", info.getStoryId());
                startActivity(intent);
            }
        }
    }

    private class downloadImage extends AsyncTask<String, Integer, String> {
        PresidentInfo info;

        public downloadImage(PresidentInfo info) {
            this.info = info;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showpDialog();
        }

        @Override
        protected String doInBackground(String... url) {
            try {

                downloadImages(new URL(info.getPresImageUrl()), info.getPresImageName());
                downloadImages(new URL(info.getPresSignatureImageUrl()), info.getPresSignatureImageName());
                downloadImages(new URL(info.getBackgroundImageUrl()), info.getBackgroundImageName());

                info.save();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            hidepDialog();
            PresidentInfo pInfo = new PresidentInfo();
            pInfo = new Select().from(PresidentInfo.class).where("president_id=?", info.getPresId()).executeSingle();
            if (pInfo != null) {
                Intent intent = new Intent(MainActivity.this, PresidentFactsActivity.class);
                intent.putExtra("president_id", pInfo.getPresId());
                startActivity(intent);
            }
        }
    }

    private void downloadImages(URL url_, String imageName) {
        int count;
        try {
            URLConnection conexion = url_.openConnection();
            conexion.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conexion.getContentLength();

            // downlod the file
            InputStream input = new BufferedInputStream(url_.openStream());
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator  + folder_main_images + File.separator+ imageName);
            //OutputStream output = new FileOutputStream("/sdcard/" + folder_main_images + "/" + imageName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }

    }
    @Override
    protected void onRestart(){
        super.onRestart();
        if(btnContents.isSelected()){
            conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
            if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                new getStoriesList().execute();
            } else {
                showStoriesList();
            }
        }else if(btnPresidential.isSelected()){
            conn = NetworkChangeReceiver.getConnectivityStatus(MainActivity.this);
            if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                new getPresidentsList().execute();
            } else {
                showPresidentsList();
            }
        }
    }
}
