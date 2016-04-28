package com.ingentive.nahad.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ingentive.nahad.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageButton ibtnEmail,ibtnProcees;
    private EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }
    public void initialize (){
        ibtnEmail = (ImageButton)findViewById(R.id.ibtn_enter_email);
        ibtnProcees = (ImageButton) findViewById(R.id.ibtn_proceed);
        etEmail = (EditText) findViewById(R.id.et_enter_email);

        ibtnEmail.setOnClickListener(this);
        ibtnProcees.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtn_enter_email:
                etEmail.setVisibility(View.VISIBLE);

//                ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE))
//                        .showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);

                ibtnEmail.setVisibility(View.INVISIBLE);
                InputMethodManager inputMgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.toggleSoftInput(0, 0);
                etEmail.findFocus();
                break;
            case R.id.ibtn_proceed:
                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(LoginActivity.this,PDFDownloadActivity.class);
//                startActivity(intent);
                //Toast.makeText(LoginActivity.this,"Proceed button click",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
