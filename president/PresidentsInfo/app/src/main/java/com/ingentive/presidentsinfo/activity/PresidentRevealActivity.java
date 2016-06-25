package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.PresidentInfo;
import com.ingentive.presidentsinfo.activeandroid.StoryInfo;

import java.io.File;

public class PresidentRevealActivity extends Activity {

    private TextView tvHeader, tvBactToStory, tvPF, tvMoral;
    private ImageView ivPresident;
    private Button btnMoral;
    int presidentId;
    int storyId;
    StoryInfo storyInfo;
    private String folder_main_images = "Presidents_Stories/Images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_reveal);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvBactToStory = (TextView) findViewById(R.id.tv_back_story);
        tvPF = (TextView) findViewById(R.id.tv_pf);
        tvMoral = (TextView) findViewById(R.id.tv_moral);
        ivPresident = (ImageView) findViewById(R.id.iv_president);
        btnMoral = (Button) findViewById(R.id.btn_moral);
        tvBactToStory.setText("< Back to Story");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presidentId = extras.getInt("president_id");
            storyId = extras.getInt("story_id");
        }
        storyInfo=new Select().from(StoryInfo.class).where("story_id=? AND president_id=?",storyId,presidentId).executeSingle();
        PresidentInfo presidentInfo=new PresidentInfo();
        presidentInfo=new Select().from(PresidentInfo.class).where("president_id=?",presidentId).executeSingle();

        if(storyInfo!=null&&presidentInfo!=null){
            tvHeader.setText(presidentInfo.getPresName().toUpperCase().toString());

            String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folder_main_images + File.separator + presidentInfo.getPresImageName();
            Bitmap bitmap = StringToBitMap(mFilePath);
            //ivPf.setImageBitmap(bitmap);
            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            ivPresident.setBackgroundDrawable(ob);
        }

        btnMoral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMoral.setVisibility(View.VISIBLE);
                tvMoral.setText(Html.fromHtml(storyInfo.getStoryMoral()));
            }
        });
        tvBactToStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PresidentRevealActivity.this,ReadStoryActivity.class);
                i.putExtra("story_id",storyInfo.getStoryId());
                startActivity(i);
                finish();
            }
        });
        tvPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PresidentRevealActivity.this,PresidentFactsActivity.class);
                i.putExtra("president_id",storyInfo.getPresidentId());
                startActivity(i);
                finish();
            }
        });
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
}
