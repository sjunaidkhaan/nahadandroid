package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.ingentive.nahad.R;

public class ContentsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            String itemname = b.get("itemname").toString();
            Toast.makeText(getApplication(),""+itemname,Toast.LENGTH_LONG).show();
        }

//        final Dialog dialog = new Dialog(ContentsActivity.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialogbox);
//        dialog.show();
    }
}
