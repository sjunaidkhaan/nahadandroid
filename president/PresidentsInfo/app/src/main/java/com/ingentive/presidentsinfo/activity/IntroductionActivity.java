package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.activeandroid.SettingsModel;

public class IntroductionActivity extends Activity {

    private TextView tvHeading,tvText;
    int textSize ;
    private SettingsModel settingsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        settingsModel = new SettingsModel();
        settingsModel = new Select().from(SettingsModel.class).executeSingle();
        if (settingsModel == null) {
            SettingsModel model = new SettingsModel();
            model.randomize = "on";
            model.fontSize = 18;
            model.save();
            textSize = 18;
        } else {
            textSize = settingsModel.getFontSize();
        }

        tvHeading = (TextView)findViewById(R.id.tv_heading);
        tvText = (TextView)findViewById(R.id.tv_text);

        tvHeading.setText("WHY SHOULD\nANYONE CARE\nABOUT MILLARD\nFILLMORE OR\nGERALD FORD?");
        tvText.setText("Android is an operating system for mobile devices such as smartphones and tablet computers. It is developed by the Open Handset Alliance led by Google. It's is built on a Linux foundation. Google purchased the initial developer of the software, Android Inc., in 2005. The unveiling of the Android distribution on November 5, 2007 was announced with the founding of the Open Handset Alliance, a consortium of 84 hardware, software, and telecommunication companies devoted to advancing open standards for mobile devices.\n" +
                "This alliance shares a common goal of fostering innovation on mobile devices and giving consumers a far better user experience than much of what is available on today's mobile platforms. By providing developers a new level of openness that enables them to work more collaboratively, Android will accelerate the pace at which new and compelling mobile services are made available to consumers. Android is often symbolized by the green robot to the right.\n" +
                "\n" +
                "Android has evolved rapidly since its launch. Google has named all projects after a dessert. The main releases are listed below, this is nothing you have to memorize, it's just to illustrate the rapid pace of development and all the innovations. Android is developed \"on Internet time\", that is much faster than the old style of development (for example Windows releases which are typically several years apart).Android is an operating system for mobile devices such as smartphones and tablet computers. It is developed by the Open Handset Alliance led by Google. It's is built on a Linux foundation. Google purchased the initial developer of the software, Android Inc., in 2005. The unveiling of the Android distribution on November 5, 2007 was announced with the founding of the Open Handset Alliance, a consortium of 84 hardware, software, and telecommunication companies devoted to advancing open standards for mobile devices.\n" +
                "This alliance shares a common goal of fostering innovation on mobile devices and giving consumers a far better user experience than much of what is available on today's mobile platforms. By providing developers a new level of openness that enables them to work more collaboratively, Android will accelerate the pace at which new and compelling mobile services are made available to consumers. Android is often symbolized by the green robot to the right.\n" +
                "\n" +
                "Android has evolved rapidly since its launch. Google has named all projects after a dessert. The main releases are listed below, this is nothing you have to memorize, it's just to illustrate the rapid pace of development and all the innovations. Android is developed \"on Internet time\", that is much faster than the old style of development (for example Windows releases which are typically several years apart).");
        tvText.setTextSize(textSize);
    }
}
