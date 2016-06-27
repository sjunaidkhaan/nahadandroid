package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ingentive.nahad.R;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.NetworkChangeReceiver;
import com.ingentive.nahad.common.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageButton ibtnEmail, ibtnProcees;
    private EditText etEmail;
    private String urlLogin = "http://pdfcms.azurewebsites.net/api/subscribers/auth";
    private int conn = 0;
    private ProgressDialog pDialog;
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    //String email ="admin@gmail.com";
    boolean email = false;

    // public static final String Email = "emailKey";
    public static SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "MyPrefs";
    public static SharedPreferences prefs;
    String mEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        if (prefs.getString(KEY_EMAIL, null) != null && prefs.getString(KEY_TOKEN, null) != null) {
            mEmail = prefs.getString("email", null);
            if (mEmail != null) {
                ibtnEmail.setVisibility(View.INVISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                etEmail.setText(mEmail+"");
                conn = NetworkChangeReceiver.getConnectivityStatus(LoginActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    // loginRequest();
                    new loginRequest().execute();
                } else {
                    new Thread() {
                        public void run() {
                            try {
                                sleep(1000);
                                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                intent.putExtra("intent", "splash_intent");
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                            }
                        }
                    }.start();
                }
            }
        }
    }

    public void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        ibtnEmail = (ImageButton) findViewById(R.id.ibtn_enter_email);
        ibtnProcees = (ImageButton) findViewById(R.id.ibtn_proceed);
        etEmail = (EditText) findViewById(R.id.et_enter_email);
        //etEmail.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        ibtnEmail.setOnClickListener(this);
        ibtnProcees.setOnClickListener(this);
       // exportDB(LoginActivity.this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtn_enter_email:
                email = true;
                etEmail.setVisibility(View.VISIBLE);
                etEmail.requestFocus();
//                ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE))
//                        .showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);

                ibtnEmail.setVisibility(View.INVISIBLE);
                InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.toggleSoftInput(0, 0);
                etEmail.findFocus();
                etEmail.requestFocus();
                break;
            case R.id.ibtn_proceed:
                if (email == true)
                    if (isValidEmailAddress(etEmail.getText().toString().trim())) {
                        conn = NetworkChangeReceiver.getConnectivityStatus(LoginActivity.this);
                        if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                            ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).
                                    hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
                            mEmail = etEmail.getText().toString().trim();
                            new loginRequest().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You need to enter email", Toast.LENGTH_LONG).show();

                    }
                break;
        }
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private class loginRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair(KEY_EMAIL, mEmail +
                    ""));

            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urlLogin, ServiceHandler.POST, params);
            android.util.Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    // {"STATUS":"NOT FOUND"}
                    Log.d("", "" + jsonStr);
                    // {"Message":"User not found"}
                    // {"status":true,"Email":"admin@gmail.com","token":"OKIGGMABMDQNFWWRSFXFYUUV","message":"Success"}
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String message = jsonObj.optString("Message", "");
                    String messag = jsonObj.optString("message", "");
                    if (message.equals("User not found") && messag.equals("")) {
                        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        prefs.edit().remove(mEmail).commit();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                hidepDialog();
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (message.equals("") && messag.equals("Success")) {
                        String status = jsonObj.getString("status");
                        String email = jsonObj.getString("Email");
                        String token = jsonObj.getString("token");

                        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        editor = prefs.edit();
                        editor.putString(KEY_EMAIL, email);
                        editor.putString(KEY_TOKEN, token);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        intent.putExtra("intent", "splash_intent");
                        startActivity(intent);
                        finish();

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
            hidepDialog();
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
