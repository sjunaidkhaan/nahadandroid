package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.PresidentInfo;
import com.ingentive.presidentsinfo.activeandroid.SettingsModel;
import com.ingentive.presidentsinfo.activeandroid.StoriesList;
import com.ingentive.presidentsinfo.activeandroid.StoryInfo;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.xml.sax.XMLReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PresidentFactsWebViewActivity extends Activity {

    private BottomBar mBottomBar;
    private TextView tvQuotation;
    private WebView tvPreFactsText;
    private ImageView ivSign, ivPf;
    private int presId = 0;
    private PresidentInfo presidentInfo;
    private int random;
    private boolean content = false;
    private String folder_main_images = "Presidents_Stories/Images";
    private SettingsModel settingsModel;
    private int textSize;
    private String randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_facts_web_view);
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
            presId = extras.getInt("president_id");
            presidentInfo = new PresidentInfo();
            presidentInfo = new Select().from(PresidentInfo.class).where("president_id=?", presId).executeSingle();
            if (presidentInfo != null) {
                showPresident(presidentInfo);
            }
        }

        mBottomBar.setMaxFixedTabs(2);

        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu_p_f, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //mMessageView.setText(getMessage(menuItemId, false));
                Intent intent;
                switch (menuItemId) {
                    case R.id.contents:
                        if (content == true) {
                            intent = new Intent(PresidentFactsWebViewActivity.this, MainActivity.class);
                            intent.putExtra("btn_clicked", "btn_contents");
                            startActivity(intent);
                            finish();
                            content = false;
                        } else {
                            content = true;
                        }
                        break;
                    case R.id.randomize:
                        if (randomize.equals("on")) {
                            int preId = 0;
                            List<Integer> arrayList = new ArrayList<Integer>();
                            List<PresidentInfo> presidentInfoList = new ArrayList<PresidentInfo>();
                            presidentInfoList = new Select().all().from(PresidentInfo.class).orderBy("president_id ASC").execute();
                            for (int i = 0; i < presidentInfoList.size(); i++) {
                                arrayList.add(presidentInfoList.get(i).getPresId());
                            }
                            if (arrayList.size() > 1) {
                                preId = getRandom(arrayList);
                            }
                            PresidentInfo president_info = new PresidentInfo();
                            president_info = new Select().from(PresidentInfo.class).where("president_id=?", preId).executeSingle();
                            if (president_info != null) {
                                presidentInfo = president_info;
                                presId = preId;
                                showPresident(president_info);
                            }
                        } else {
                            Toast.makeText(PresidentFactsWebViewActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        StoryInfo story_Info = new StoryInfo();
                        story_Info = new Select().from(StoryInfo.class).where("president_id=?", presId).executeSingle();
                        if (story_Info == null) {
                            intent = new Intent(PresidentFactsWebViewActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", 0);
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        } else {
                            intent = new Intent(PresidentFactsWebViewActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", story_Info.getStoryId());
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        }
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Intent intent;
                switch (menuItemId) {
                    case R.id.contents:
                        intent = new Intent(PresidentFactsWebViewActivity.this, MainActivity.class);
                        intent.putExtra("btn_clicked", "btn_contents");
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.randomize:
                        if (randomize.equals("on")) {
                            int preId = 0;
                            List<Integer> arrayList = new ArrayList<Integer>();
                            List<PresidentInfo> presidentInfoList = new ArrayList<PresidentInfo>();
                            presidentInfoList = new Select().all().from(PresidentInfo.class).orderBy("president_id ASC").execute();
                            for (int i = 0; i < presidentInfoList.size(); i++) {
                                arrayList.add(presidentInfoList.get(i).getPresId());
                            }
                            if (arrayList.size() > 1) {
                                preId = getRandom(arrayList);
                            }
                            PresidentInfo president_info = new PresidentInfo();
                            president_info = new Select().from(PresidentInfo.class).where("president_id=?", preId).executeSingle();
                            if (president_info != null) {
                                presidentInfo = president_info;
                                presId = preId;
                                showPresident(president_info);
                            }
                        } else {
                            Toast.makeText(PresidentFactsWebViewActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        StoryInfo story_Info = new StoryInfo();
                        story_Info = new Select().from(StoryInfo.class).where("president_id=?", presId).executeSingle();
                        if (story_Info == null) {
                            intent = new Intent(PresidentFactsWebViewActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", 0);
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        } else {
                            intent = new Intent(PresidentFactsWebViewActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", story_Info.getStoryId());
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        }
                        break;

                }
            }
        });
        mBottomBar.mapColorForTab(0, "#1b2330");
        mBottomBar.mapColorForTab(1, "#1b2330");
        mBottomBar.mapColorForTab(2, "#1b2330");
    }

    private void initializeViews(Bundle savedInstanceState) {
        tvPreFactsText = (WebView) findViewById(R.id.tv_p_f_text);
        tvQuotation = (TextView) findViewById(R.id.tv_quotation);
        ivSign = (ImageView) findViewById(R.id.iv_sign);
        ivPf = (ImageView) findViewById(R.id.iv_p_f);
        ScrollView parentScroll=(ScrollView)findViewById(R.id.parentScroll);
        ScrollView childScroll=(ScrollView)findViewById(R.id.childScroll);


        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),
                findViewById(R.id.myScrollingContent), savedInstanceState);


        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.v("","PARENT TOUCH");
                findViewById(R.id.childScroll).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                Log.v("","CHILD TOUCH");
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    public int getRandom(List<Integer> array) {
        int rnd = new Random().nextInt(array.size());
        random = array.get(rnd);
        if (random == presId) {
            getRandom(array);
        }
        return random;
    }

    private void showPresident(PresidentInfo presInfo) {
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main_images + File.separator + presInfo.getPresSignatureImageName();
        Bitmap bitmap_ = StringToBitMap(mFilePath);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap_);
        ivSign.setBackgroundDrawable(ob);
        //ivSign.setImageBitmap(bitmap_);
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main_images + File.separator + presInfo.getPresImageName();
        Bitmap bitmap = StringToBitMap(mFilePath);
        //ivPf.setImageBitmap(bitmap);
        ob = new BitmapDrawable(getResources(), bitmap);
        ivPf.setBackgroundDrawable(ob);
        tvQuotation.setText(android.text.Html.fromHtml(presInfo.getPresQuotation()));

        String str = presInfo.getPresFact();
        str = "<font color='white'>" + str + "</font>";
        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/mrseavesot-roman.ttf\")}body {font-family: MyFont;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + str + pas;
        tvPreFactsText.loadUrl("about:blank");
        tvPreFactsText.loadDataWithBaseURL(null, myHtmlString, "text/html", "UTF-8", null);
        tvPreFactsText.setBackgroundColor(Color.TRANSPARENT);
        final WebSettings webSettings = tvPreFactsText.getSettings();
        webSettings.setDefaultFontSize((int) textSize);
        tvQuotation.setTextSize(textSize);
    }

    public Bitmap StringToBitMap(String path) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Log.d("", "" + e.getMessage());
            return null;
        }
    }
    @Override
    public void onRestart(){
        super.onRestart();
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
        if (presidentInfo != null) {
            showPresident(presidentInfo);
        }
    }
}

