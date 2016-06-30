package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
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
import java.util.Random;

public class PresidentFactsActivity extends Activity {

    private BottomBar mBottomBar;
    private TextView tvPreFactsText, tvQuotation;
    private ImageView ivSign, ivPf;
    private Select select;
    private int presId = 0;
    private PresidentInfo presidentInfo;
    private int min = 0;
    private int max = 0;
    private Random r;
    private int random;
    private boolean content = false;
    private StoriesList storiesList;
    private String folder_main_images = "Presidents_Stories/Images";
    private SettingsModel settingsModel;
    int textSize;
    String randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_facts);
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
                            intent = new Intent(PresidentFactsActivity.this, MainActivity.class);
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
                            int count = new Select().all().from(PresidentInfo.class).orderBy("president_id ASC").execute().size();
                            int presId = getRandom(count);//r.nextInt((max - min) + 1) + min;
                            PresidentInfo president_info = new PresidentInfo();
                            president_info = new Select().from(PresidentInfo.class).where("president_id=?", presId).executeSingle();
                            if (president_info != null) {
                                showPresident(president_info);
                            }
                        }else {
                            Toast.makeText(PresidentFactsActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        StoryInfo story_Info = new StoryInfo();
                        story_Info = new Select().from(StoryInfo.class).where("president_id=?", presId).executeSingle();
                        if (story_Info == null) {
                            intent = new Intent(PresidentFactsActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", 0);
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        } else {
                            intent = new Intent(PresidentFactsActivity.this, SettingsActivity.class);
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
                        intent = new Intent(PresidentFactsActivity.this, MainActivity.class);
                        intent.putExtra("btn_clicked", "btn_contents");
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.randomize:
                        if (randomize.equals("on")) {
                            int count = new Select().all().from(PresidentInfo.class).orderBy("president_id ASC").execute().size();
                            int presId = getRandom(count);//r.nextInt((max - min) + 1) + min;
                            PresidentInfo president_info = new PresidentInfo();
                            president_info = new Select().from(PresidentInfo.class).where("president_id=?", presId).executeSingle();
                            if (president_info != null) {
                                showPresident(president_info);
                            }
                        } else {
                            Toast.makeText(PresidentFactsActivity.this, "Please Turn On Randomization", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.settings:
                        StoryInfo story_Info = new StoryInfo();
                        story_Info = new Select().from(StoryInfo.class).where("president_id=?", presId).executeSingle();
                        if (story_Info == null) {
                            intent = new Intent(PresidentFactsActivity.this, SettingsActivity.class);
                            intent.putExtra("story_id", 0);
                            intent.putExtra("from", "president");
                            intent.putExtra("president_id", presId);
                            startActivity(intent);
                        } else {
                            intent = new Intent(PresidentFactsActivity.this, SettingsActivity.class);
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
        tvPreFactsText = (TextView) findViewById(R.id.tv_p_f_text);
        tvQuotation = (TextView) findViewById(R.id.tv_quotation);
        ivSign = (ImageView) findViewById(R.id.iv_sign);
        ivPf = (ImageView) findViewById(R.id.iv_p_f);

        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.myCoordinator),
                findViewById(R.id.myScrollingContent), savedInstanceState);
    }

    private int getRandom(int count) {
        min = 1;
        max = count;
        r = new Random();
        random = r.nextInt((max - min) + 1) + min;
        if (random == presId && count > 1) {
            getRandom(count);
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

        tvQuotation.setText(Html.fromHtml(presInfo.getPresQuotation()));
        tvPreFactsText.setText(Html.fromHtml(presInfo.getPresFact(), null, new UlTagHandler()));
        tvPreFactsText.setTextSize(textSize);
        tvQuotation.setTextSize(textSize);
    }

    public Bitmap StringToBitMap(String path) {
        try {
            //byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Log.d("", "" + e.getMessage());
            return null;
        }
    }

    public class UlTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equals("ul") && !opening)
                output.append("\n");
            if (tag.equals("li") && opening)
                output.append("\n•");

            if (tag.equals("tr") && !opening)
                output.append("\n");
            if (tag.equals("th") && opening)
                output.append("\t");
            if (tag.equals("td") && opening)
                output.append("\t");
        }
    }
}

