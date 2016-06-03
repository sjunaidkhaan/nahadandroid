package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageButton ibtnEmail, ibtnProcees;
    private EditText etEmail;
    private boolean is3g;
    private boolean isWifi;
    private String urlLogin = "http://pdfcms.azurewebsites.net/api/subscribers/auth";
    //private String urlLogin = "http://elanportal.elan.pk/elanportal/elan_api/elan/apis/login_api";

    private ProgressDialog pDialog;
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    //String email ="admin@gmail.com";
    boolean email = false;

    // public static final String Email = "emailKey";
    public static SharedPreferences.Editor editor;
    public static String MyPREFERENCES = "MyPrefs";
    public static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        if (prefs.getString(KEY_EMAIL, null) != null && prefs.getString(KEY_TOKEN, null) != null) {
            etEmail.setVisibility(View.INVISIBLE);
            ibtnProcees.setVisibility(View.INVISIBLE);
            ibtnEmail.setVisibility(View.INVISIBLE);
            new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                    }
                }
            }.start();

        }
    }

    public void initialize() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        ibtnEmail = (ImageButton) findViewById(R.id.ibtn_enter_email);
        ibtnProcees = (ImageButton) findViewById(R.id.ibtn_proceed);
        etEmail = (EditText) findViewById(R.id.et_enter_email);
        ibtnEmail.setOnClickListener(this);
        ibtnProcees.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtn_enter_email:
                email = true;
                etEmail.setVisibility(View.VISIBLE);
//                ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE))
//                        .showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);

                ibtnEmail.setVisibility(View.INVISIBLE);
                InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.toggleSoftInput(0, 0);
                etEmail.findFocus();
                break;
            case R.id.ibtn_proceed:
                if (email == true)
                    if (!etEmail.toString().trim().isEmpty()) {
                        if (wifiChecker() == true) {
                            ((InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE)).
                                    hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
                            loginRequest();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //etEmail.setError("You need to enter email");
                        Toast.makeText(getApplicationContext(), "You need to enter email", Toast.LENGTH_LONG).show();

                    }
                break;
        }
    }

    private boolean wifiChecker() {
        // TODO Auto-generated method stub
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        Log.v("", is3g + " ConnectivityManager Test " + isWifi);
        if (!is3g && !isWifi) {
            return false;
        }
        return true;
    }

    public void loginRequest() {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidepDialog();
                        try {
                            Log.d("",""+response);
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            String email = jsonObj.getString("Email");
                            String token = jsonObj.getString("token");
                            String message = jsonObj.getString("message");
                            if (message.equals("Success")) {
                                prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                editor = prefs.edit();
                                editor.putString(KEY_EMAIL, email);
                                editor.putString(KEY_TOKEN, token);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        String errorStr = error.getMessage();
                        Toast.makeText(LoginActivity.this, "error: "+errorStr, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, etEmail.getText().toString());
                //params.put(KEY_EMAIL, etEmail.getText().toString());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
