package com.ingentive.presidentsinfo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.ingentive.presidentsinfo.R;
import com.ingentive.presidentsinfo.adapter.TextAdapter;
import com.ingentive.presidentsinfo.model.TextModel;

import java.util.ArrayList;
import java.util.List;

public class ContentsActivity extends Activity implements View.OnClickListener {

    private Button btnContents, btnPresidential, btnCredits;
    private ListView listView;
    private TextAdapter mAdapter;
    private TextModel model = new TextModel();
    private List<TextModel> list = new ArrayList<TextModel>();
    private String seleted_btn = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        initialize();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void initialize() {
        btnContents = (Button) findViewById(R.id.btn_contents);
        btnPresidential = (Button) findViewById(R.id.btn_p_f);
        btnCredits = (Button) findViewById(R.id.btn_credits);
        listView = (ListView) findViewById(R.id.listView);
        btnContents.setOnClickListener(this);
        btnPresidential.setOnClickListener(this);
        btnCredits.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            switch (extras.getString("btn_clicked").toString()) {
                case "btn_contents":
                    seleted_btn = "";
                    seleted_btn = "btn_contents";
                    btnContents.setPressed(false);
                    btnPresidential.setPressed(true);
                    btnCredits.setPressed(true);
                    contentsArray();
                    break;
                case "btn_p_f":
                    seleted_btn = "";
                    seleted_btn = "btn_p_f";
                    pfArray();
                    btnContents.setPressed(true);
                    btnPresidential.setPressed(false);
                    btnCredits.setPressed(true);
                    break;
                case "btn_credits":
                    seleted_btn = "";
                    seleted_btn = "btn_credits";
                    creditsArray();
                    btnContents.setPressed(true);
                    btnPresidential.setPressed(true);
                    btnCredits.setPressed(false);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_contents:
                contentsArray();
                btnContents.setPressed(false);
                btnPresidential.setPressed(true);
                btnCredits.setPressed(true);
                break;
            case R.id.btn_p_f:
                btnPresidential.setPressed(false);
                btnContents.setPressed(true);
                btnCredits.setPressed(true);
                pfArray();
                break;
            case R.id.btn_credits:
                creditsArray();
                btnCredits.setPressed(false);
                btnContents.setPressed(true);
                btnPresidential.setPressed(true);
                break;
        }
    }

    private void contentsArray() {
        list = new ArrayList<TextModel>();
        String[] contents = {"FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL"};
        for (int i = 0; i < contents.length; i++) {
            model = new TextModel();
            model.setText(contents[i]);
            list.add(model);
        }
        mAdapter = new TextAdapter(getApplication(), list, R.layout.custom_row_text);
        listView.setAdapter(mAdapter);
    }

    private void pfArray() {
        list = new ArrayList<TextModel>();
        String[] contents = {"GEORGE WASHINGTON", "THONMAS JEFFERSON", "JAMES MADISON", "ANDREW JACKSON",
                "WILLIAM HENRY HARRISON", "JOHN TYLER", "JAMES K. POLK", "ZACHARY TAYLOR", "ABRAHAM LINCOLN",
                "RUTHERFORD B. HAYES", "JAMES GARFIELD", "CHESTER ARTHUR"};
        for (int i = 0; i < contents.length; i++) {
            model = new TextModel();
            model.setText(contents[i]);
            list.add(model);
        }
        mAdapter = new TextAdapter(getApplication(), list, R.layout.custom_row_text);
        listView.setAdapter(mAdapter);
    }

    private void creditsArray() {
        list = new ArrayList<TextModel>();
        String[] contents = {"FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL",
                "FOUR BULLETS AND THREE HORSES", "EVERY SACRIFICE MADE", "NOT SAVED BY THE BELL"};
        for (int i = 0; i < contents.length; i++) {
            model = new TextModel();
            model.setText(contents[i]);
            list.add(model);
        }
        mAdapter = new TextAdapter(getApplication(), list, R.layout.custom_row_text);
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        switch (seleted_btn) {
            case "btn_contents":
                seleted_btn = "";
                seleted_btn = "btn_contents";
                btnContents.setPressed(false);
                btnPresidential.setPressed(true);
                btnCredits.setPressed(true);
                contentsArray();
                break;
            case "btn_p_f":
                seleted_btn = "";
                seleted_btn = "btn_p_f";
                pfArray();
                btnContents.setPressed(true);
                btnPresidential.setPressed(false);
                btnCredits.setPressed(true);
                break;
            case "btn_credits":
                seleted_btn = "";
                seleted_btn = "btn_credits";
                creditsArray();
                btnContents.setPressed(true);
                btnPresidential.setPressed(true);
                btnCredits.setPressed(false);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
