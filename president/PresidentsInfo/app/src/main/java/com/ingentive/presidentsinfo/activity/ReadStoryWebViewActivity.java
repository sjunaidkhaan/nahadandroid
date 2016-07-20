package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.PresTSInFirstStory;
import com.ingentive.presidentsinfo.activeandroid.PresidentInfo;
import com.ingentive.presidentsinfo.activeandroid.PresidentsList;
import com.ingentive.presidentsinfo.activeandroid.SettingsModel;
import com.ingentive.presidentsinfo.activeandroid.StoriesList;
import com.ingentive.presidentsinfo.activeandroid.StoryInfo;
import com.ingentive.presidentsinfo.common.NetworkChangeReceiver;
import com.ingentive.presidentsinfo.common.ServiceHandler;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ReadStoryWebViewActivity extends Activity {


    private BottomBar mBottomBar;
    private WebView tvLongDescription;
    private Button btnPrisidentFacts, btnQuickRead, btnRead;

    private TextView duration;
    private int backwardTime = 30000;
    private ImageView ivPause, ivRepeat;
    private int storyId = 0;
    private StoryInfo storyInfo;
    Select select;
    private MediaPlayer mediaPlayer;
    private String folder_main = "Presidents_Stories";
    private ScrollView scrollView;
    private LinearLayout audio_layout;
    private android.support.design.widget.CoordinatorLayout myCoordinator;
    private TextView tvHeading, tvQuickRead, tvHeading_;
    private double finalTime = 0, startTime = 0;
    private SeekBar seekbar;
    private Handler myHandler = new Handler();
    private int random;
    private boolean content = false;
    private int conn = 0;
    private ProgressDialog mProgressDialog;
    public String urlPresidentInfo = "http://yourbrand.pk/presidentrevealed/services/president_info";
    private String folder_main_images = "Presidents_Stories/Images";
    int seekBarChangeValue = 0;
    private boolean playSong = false;
    private SettingsModel settingsModel;
    private int textSize;
    private String randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story_web_view);
        initializeViews(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        settingsModel = new SettingsModel();
        settingsModel = new Select().from(SettingsModel.class).executeSingle();
        if (settingsModel == null) {
            SettingsModel model = new SettingsModel();
            model.randomize = "on";
            model.fontSize = 18;
            model.save();
            textSize = 18;
            randomize = "on";
        } else {
            textSize = settingsModel.getFontSize();
            randomize = settingsModel.getRandomize();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            select = new Select();
            storyId = extras.getInt("story_id");
            storyInfo = new StoryInfo();
            storyInfo = select.from(StoryInfo.class).where("story_id=?", storyId).executeSingle();
            if (storyInfo != null) {
                showStory(storyInfo);
            }
        }
        btnRead.setSelected(true);
        btnQuickRead.setSelected(false);

    }

    private void initializeViews(Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        tvHeading_ = (TextView) findViewById(R.id.tv_title);
        tvQuickRead = (TextView) findViewById(R.id.tv_quick_read);
        myCoordinator = (android.support.design.widget.CoordinatorLayout) findViewById(R.id.myCoordinator);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        audio_layout = (LinearLayout) findViewById(R.id.audio_layout);
        ivPause = (ImageView) findViewById(R.id.ibtn_pause);
        ivRepeat = (ImageView) findViewById(R.id.ibtn_repeat);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        duration = (TextView) findViewById(R.id.tv_song_duration);
        tvHeading = (TextView) findViewById(R.id.tv_heading);
        tvLongDescription = (WebView) findViewById(R.id.tv_story);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        btnPrisidentFacts = (Button) findViewById(R.id.btn_presidents_facts);
        btnQuickRead = (Button) findViewById(R.id.btn_quick_read);
        btnRead = (Button) findViewById(R.id.btn_read);

        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playSong == true) {
                    mediaPlayer.pause();
                    ivPause.setImageResource(R.drawable.ico_audio);
                    playSong = false;
                } else {
                    mediaPlayer.start();
                    ivPause.setImageResource(R.drawable.icon_pause);
                    playSong = true;
                }
            }
        });
        ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp1 = (int) startTime;

                if ((temp1 - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                mediaPlayer.seekTo((int) seekBarChangeValue);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarChangeValue = progress;
            }
        });
        btnQuickRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_layout.setVisibility(View.GONE);
                tvHeading.setText(storyInfo.getDescriptionHeading().toUpperCase());
                String str = storyInfo.getShortDescription();
                str = "<font color='black'>" + str + "</font>";
                String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/mrseavesot-roman.ttf\")}body {font-family: MyFont;}</style></head><body>";
                String pas = "</body></html>";
                String myHtmlString = pish + str + pas;
                tvLongDescription.loadUrl("about:blank");
                tvLongDescription.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
                tvLongDescription.setBackgroundColor(Color.TRANSPARENT);
                final WebSettings webSettings = tvLongDescription.getSettings();
                webSettings.setDefaultFontSize((int) textSize);
                mediaPlayer.stop();
                btnRead.setSelected(false);
                btnQuickRead.setSelected(true);
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_layout.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                // audio_layout.setVisibility(View.VISIBLE);
                myCoordinator.setVisibility(View.VISIBLE);
                showStory(storyInfo);
                mediaPlayer.stop();
                btnRead.setSelected(true);
                btnQuickRead.setSelected(false);
            }
        });
        btnPrisidentFacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresidentInfo presidentInfo = new PresidentInfo();
                PresidentsList presidentsModel = new PresidentsList();
                presidentsModel = new Select().from(PresidentsList.class).where("president_id=?", storyInfo.getPresidentId()).executeSingle();
                StoriesList storyList = new Select().from(StoriesList.class).where("story_id=?", storyInfo.getStoryId()).executeSingle();
                presidentInfo = new Select().from(PresidentInfo.class).where("president_id=?", storyInfo.getPresidentId()).executeSingle();

                PresTSInFirstStory firstStory = new PresTSInFirstStory();
                firstStory = new Select().from(PresTSInFirstStory.class).executeSingle();
                if (firstStory != null && presidentInfo != null && firstStory.presId == storyInfo.getPresidentId() && presidentInfo.getTimeStamp() < firstStory.getPresidentTimeStamp()) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(ReadStoryWebViewActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getPresidentInfo(storyInfo.getPresidentId(), 0).execute();
                        mediaPlayer.stop();
                    } else {
                        Toast.makeText(ReadStoryWebViewActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                    }
                } else if (presidentInfo == null) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(ReadStoryWebViewActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        new getPresidentInfo(storyInfo.getPresidentId(), 0).execute();
                        mediaPlayer.stop();
                    } else {
                        Toast.makeText(ReadStoryWebViewActivity.this, "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                    }
                }
                else if (presidentsModel != null && presidentInfo.getTimeStamp() < presidentsModel.getTimeStamp()) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(ReadStoryWebViewActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        mediaPlayer.stop();
                        new getPresidentInfo(presidentInfo.getPresId(), presidentInfo.getTimeStamp()).execute();
                    } else {
                        Intent i = new Intent(ReadStoryWebViewActivity.this, PresidentRevealActivity.class);
                        i.putExtra("president_id", storyInfo.getPresidentId());
                        i.putExtra("story_id", storyInfo.getStoryId());
                        startActivity(i);
                        mediaPlayer.stop();
                        finish();
                    }
                } else if (presidentInfo.getTimeStamp() < storyList.getPresidentTimeStamp()) {
                    conn = NetworkChangeReceiver.getConnectivityStatus(ReadStoryWebViewActivity.this);
                    if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                        mediaPlayer.stop();
                        new getPresidentInfo(presidentInfo.getPresId(), presidentInfo.getTimeStamp()).execute();
                    } else {
                        Intent i = new Intent(ReadStoryWebViewActivity.this, PresidentRevealActivity.class);
                        i.putExtra("president_id", storyInfo.getPresidentId());
                        i.putExtra("story_id", storyInfo.getStoryId());
                        startActivity(i);
                        mediaPlayer.stop();
                        finish();
                    }
                } else {
                    Intent i = new Intent(ReadStoryWebViewActivity.this, PresidentRevealActivity.class);
                    i.putExtra("president_id", storyInfo.getPresidentId());
                    i.putExtra("story_id", storyInfo.getStoryId());
                    startActivity(i);
                    mediaPlayer.stop();
                    finish();
                }
            }
        });
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),
                findViewById(R.id.myScrollingContent), savedInstanceState);

        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //mMessageView.setText(getMessage(menuItemId, false));
                Intent intent;
                switch (menuItemId) {
                    case R.id.audio:
                        mediaPlayer.stop();
                        if (content == true) {
                            if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main + File.separator + storyInfo.getStoryAudioName()).exists()) {
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main + File.separator + storyInfo.getStoryAudioName();
                                try {
                                    audio_layout.setVisibility(View.VISIBLE);
                                    mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setDataSource(path);
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                    finalTime = mediaPlayer.getDuration();
                                    ivPause.setImageResource(R.drawable.icon_pause);
                                    playSong = true;
                                } catch (IOException e) {
                                }
                                Log.i("Sd Card1 Path", path);
                            } else {
                                Toast.makeText(getApplication(), "file not exist", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            content = true;
                        }
                        break;
                    case R.id.contents:
                        //imbtnPause.setImageResource(R.drawable.ico_audio);
                        audio_layout.setVisibility(View.GONE);
                        mediaPlayer.stop();
                        intent = new Intent(ReadStoryWebViewActivity.this, MainActivity.class);
                        intent.putExtra("btn_clicked", "btn_contents");
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.randomize:
                        if (randomize.equals("on")) {
                            audio_layout.setVisibility(View.GONE);
                            mediaPlayer.stop();
                            //getRandom();
                            int sId = 0;
                            List<Integer> arrayList = new ArrayList<Integer>();
                            List<StoryInfo> storyInfoList = new ArrayList<StoryInfo>();
                            storyInfoList = new Select().all().from(StoryInfo.class).orderBy("story_id ASC").execute();
                            for (int i = 0; i < storyInfoList.size(); i++) {
                                arrayList.add(storyInfoList.get(i).getStoryId());
                            }
                            if (arrayList.size() > 1) {
                                sId = getRandom(arrayList);
                            }
                            StoryInfo story_info = new StoryInfo();
                            story_info = new Select().from(StoryInfo.class).where("story_id=?", sId).executeSingle();
                            if (story_info != null) {
                                storyInfo = story_info;
                                storyId = sId;
                                showStory(story_info);
                            }
                        } else {
                            Toast.makeText(ReadStoryWebViewActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        audio_layout.setVisibility(View.GONE);
                        mediaPlayer.stop();
                        //imbtnPause.setImageResource(R.drawable.ico_audio);
                        intent = new Intent(ReadStoryWebViewActivity.this, SettingsActivity.class);
                        intent.putExtra("story_id", storyId);
                        intent.putExtra("from", "story");
                        intent.putExtra("president_id", 0);
                        startActivity(intent);
                        //finish();
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                //Toast.makeText(getApplicationContext(), getMessage(menuItemId, true), Toast.LENGTH_SHORT).show();
                switch (menuItemId) {
                    case R.id.audio:
                        mediaPlayer.stop();
                        if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main + File.separator + storyInfo.getStoryAudioName()).exists()) {
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main + File.separator + storyInfo.getStoryAudioName();
                            try {
                                audio_layout.setVisibility(View.VISIBLE);
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(path);
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                finalTime = mediaPlayer.getDuration();
                                ivPause.setImageResource(R.drawable.icon_pause);
                                playSong = true;
                                seekbar.setMax((int) finalTime);
                                seekbar.setProgress(0);
                                myHandler.postDelayed(UpdateSongTime, 1000);
                            } catch (IOException e) {

                            }
                            Log.i("Sd Card1 Path", path);
                        } else {
                            Toast.makeText(getApplication(), "file not exist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.randomize:
                        if (randomize.equals("on")) {
                            audio_layout.setVisibility(View.GONE);
                            mediaPlayer.stop();
                            int sId = 0;
                            List<Integer> arrayList = new ArrayList<Integer>();
                            List<StoryInfo> storyInfoList = new ArrayList<StoryInfo>();
                            storyInfoList = new Select().all().from(StoryInfo.class).orderBy("story_id ASC").execute();
                            for (int i = 0; i < storyInfoList.size(); i++) {
                                arrayList.add(storyInfoList.get(i).getStoryId());
                            }
                            if (arrayList.size() > 1) {
                                sId = getRandom(arrayList);
                            }
                            StoryInfo story_info = new StoryInfo();
                            story_info = new Select().from(StoryInfo.class).where("story_id=?", sId).executeSingle();
                            if (story_info != null) {
                                storyInfo = story_info;
                                storyId = sId;
                                showStory(story_info);
                            }
                        } else {
                            Toast.makeText(ReadStoryWebViewActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        audio_layout.setVisibility(View.GONE);
                        mediaPlayer.stop();
                        //imbtnPause.setImageResource(R.drawable.ico_audio);
                        Intent intent = new Intent(ReadStoryWebViewActivity.this, SettingsActivity.class);
                        intent.putExtra("story_id", storyId);
                        intent.putExtra("from", "story");
                        intent.putExtra("president_id", 0);
                        startActivity(intent);
                        //finish();
                        break;
                }
            }
        });
        mBottomBar.mapColorForTab(0, "#5e4938");
        mBottomBar.mapColorForTab(1, "#5e4938");
        mBottomBar.mapColorForTab(2, "#5e4938");
        mBottomBar.mapColorForTab(3, "#5e4938");
    }

    private void showStory(StoryInfo sInfo) {
        tvHeading.setText(sInfo.getDescriptionHeading().toUpperCase());
        String str = sInfo.getLongDescription();
        str = "<font color='black'>" + str + "</font>";
        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/mrseavesot-roman.ttf\")}body {font-family: MyFont;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + str + pas;
        tvLongDescription.loadUrl("about:blank");
        tvLongDescription.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
        tvLongDescription.setBackgroundColor(Color.TRANSPARENT);
        final WebSettings webSettings = tvLongDescription.getSettings();
        webSettings.setDefaultFontSize((int) textSize);
    }

    public int getRandom(List<Integer> array) {
        int rnd = new Random().nextInt(array.size());
        random = array.get(rnd);
        if (random == storyId) {
            getRandom(array);
        }
        return random;
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            duration.setText(String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours((long) startTime),
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) startTime)),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

            seekbar.setProgress((int) startTime);
            myHandler.postDelayed(this, 1000);
            double totalTime = mediaPlayer.getDuration();
            Log.i("totalTime :  " + totalTime, "  currentPosition :  " + startTime);
            if (startTime >= totalTime) {
                mediaPlayer.stop();
                audio_layout.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnRead.setPressed(true);
        btnQuickRead.setPressed(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        // mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnRead.setPressed(true);
        btnQuickRead.setPressed(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
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
    public void onBackPressed() {
        mediaPlayer.stop();
        finish();
        return;
    }


    private class getPresidentInfo extends AsyncTask<Void, Void, Void> {
        int presiId = 0;
        int timeStamp = 0;
        PresidentInfo mPresInfoUpdate = new PresidentInfo();
        PresidentInfo mPresInfoNew = new PresidentInfo();

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

            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("president_id", presiId + ""));
            params.add(new BasicNameValuePair("timestamp", timeStamp + ""));

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlPresidentInfo + "/" + System.currentTimeMillis(), ServiceHandler.POST, params);
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

                        mPresInfoUpdate = new PresidentInfo();
                        mPresInfoUpdate = new Select().from(PresidentInfo.class).where("president_id=?", storyInfo.getPresidentId()).executeSingle();
                        if (mPresInfoUpdate == null) {
                            mPresInfoNew = new PresidentInfo();
                            mPresInfoNew.presId = presId;
                            mPresInfoNew.presName = presName;
                            mPresInfoNew.presImageUrl = presImageUrl;
                            mPresInfoNew.presImageName = presId + "_president_" + presName + ".png";
                            mPresInfoNew.presSignatureImageUrl = presSignatureImageUrl;
                            mPresInfoNew.presSignatureImageName = presId + "_sign_" + presName + ".png";
                            mPresInfoNew.presQuotation = presQuotation;
                            mPresInfoNew.backgroundImageUrl = backgroundImageUrl;
                            mPresInfoNew.backgroundImageName = presId + "_bg_" + presName + ".png";
                            mPresInfoNew.presFact = presFact;
                            mPresInfoNew.timeStamp = timeStamp;
                            try {

                                downloadImages(new URL(presImageUrl), presId + "_president_" + presName + ".png");
                                downloadImages(new URL(presSignatureImageUrl), presId + "_sign_" + presName + ".png");
                                downloadImages(new URL(backgroundImageUrl), presId + "_bg_" + presName + ".png");
                                mPresInfoNew.save();
                            } catch (Exception e) {
                            }
                        } else if (mPresInfoUpdate.getTimeStamp() < timeStamp) {
                            mPresInfoUpdate.presId = presId;
                            mPresInfoUpdate.presName = presName;
                            mPresInfoUpdate.presImageUrl = presImageUrl;
                            mPresInfoUpdate.presImageName = presId + "_president_" + presName + ".png";
                            mPresInfoUpdate.presSignatureImageUrl = presSignatureImageUrl;
                            mPresInfoUpdate.presSignatureImageName = presId + "_sign_" + presName + ".png";
                            mPresInfoUpdate.presQuotation = presQuotation;
                            mPresInfoUpdate.backgroundImageUrl = backgroundImageUrl;
                            mPresInfoUpdate.backgroundImageName = presId + "_bg_" + presName + ".png";
                            mPresInfoUpdate.presFact = presFact;
                            mPresInfoUpdate.timeStamp = timeStamp;
                            try {

                                downloadImages(new URL(presImageUrl), presId + "_president_" + presName + ".png");
                                downloadImages(new URL(presSignatureImageUrl), presId + "_sign_" + presName + ".png");
                                downloadImages(new URL(backgroundImageUrl), presId + "_bg_" + presName + ".png");
                                mPresInfoUpdate.save();
                            } catch (Exception e) {
                            }
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

            hidepDialog();
            PresidentInfo presidentInfo = new Select().from(PresidentInfo.class).where("president_id=?", storyInfo.getPresidentId()).executeSingle();
            if (presidentInfo != null) {
                Intent i = new Intent(ReadStoryWebViewActivity.this, PresidentRevealActivity.class);
                i.putExtra("president_id", storyInfo.getPresidentId());
                i.putExtra("story_id", storyInfo.getStoryId());
                startActivity(i);
                mediaPlayer.stop();
                finish();
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
                Intent i = new Intent(ReadStoryWebViewActivity.this, PresidentRevealActivity.class);
                i.putExtra("president_id", storyInfo.getPresidentId());
                i.putExtra("story_id", storyInfo.getStoryId());
                startActivity(i);
                mediaPlayer.stop();
                finish();
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
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main_images + File.separator + imageName);
            // OutputStream output = new FileOutputStream("/sdcard/" + folder_main_images + "/" + imageName);

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
}
