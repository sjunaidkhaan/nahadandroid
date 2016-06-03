package com.ingentive.nahad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.GlossaryAdapter;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.common.JSONRequests;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.GlossaryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class GlossaryActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean is3g;
    private boolean isWifi;
    private String email;
    private String token;
    private String urlToken = "&token=";
    private String urlGlossary = "http://pdfcms.azurewebsites.net/api/glossaries/?email=";
    private ProgressDialog pDialog;
    private List<GlossaryModel> glossaryModelList = new ArrayList<GlossaryModel>();
    private List<GlossaryModel> glossaryList = new ArrayList<GlossaryModel>();
    private ListView listView;
    private Button btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL, btnM, btnN,
            btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ;
    private RelativeLayout headerEmailLayout, headerSearchLayout, headerBookMarksLayout, headerMenuLayout;
    private View headerLayout;
    private String alphabet = "A";
    private GlossaryAdapter mAdapter;
    private GlossaryModel glossaryModel;
    private ImageView ivLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);
        initialize();
    }

    public void initialize() {
        headerLayout = findViewById(R.id.header_layout_glossary);
        headerEmailLayout = (RelativeLayout) headerLayout.findViewById(R.id.send_email_layout);
        headerMenuLayout = (RelativeLayout) headerLayout.findViewById(R.id.menu_layout);
        ivLogo = (ImageView) findViewById(R.id.logo);
        headerMenuLayout.setOnClickListener(this);
        headerEmailLayout.setOnClickListener(this);
        ivLogo.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listview);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnA = (Button) findViewById(R.id.btn_a);
        btnB = (Button) findViewById(R.id.btn_b);
        btnC = (Button) findViewById(R.id.btn_c);
        btnD = (Button) findViewById(R.id.btn_d);
        btnE = (Button) findViewById(R.id.btn_e);
        btnF = (Button) findViewById(R.id.btn_f);
        btnG = (Button) findViewById(R.id.btn_g);
        btnH = (Button) findViewById(R.id.btn_h);
        btnI = (Button) findViewById(R.id.btn_i);
        btnJ = (Button) findViewById(R.id.btn_j);
        btnK = (Button) findViewById(R.id.btn_k);
        btnL = (Button) findViewById(R.id.btn_l);
        btnM = (Button) findViewById(R.id.btn_m);
        btnN = (Button) findViewById(R.id.btn_n);
        btnO = (Button) findViewById(R.id.btn_o);
        btnP = (Button) findViewById(R.id.btn_p);
        btnQ = (Button) findViewById(R.id.btn_q);
        btnR = (Button) findViewById(R.id.btn_r);
        btnS = (Button) findViewById(R.id.btn_s);
        btnT = (Button) findViewById(R.id.btn_t);
        btnU = (Button) findViewById(R.id.btn_u);
        btnV = (Button) findViewById(R.id.btn_v);
        btnW = (Button) findViewById(R.id.btn_w);
        btnX = (Button) findViewById(R.id.btn_x);
        btnY = (Button) findViewById(R.id.btn_y);
        btnZ = (Button) findViewById(R.id.btn_z);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
        btnE.setOnClickListener(this);
        btnF.setOnClickListener(this);
        btnG.setOnClickListener(this);
        btnH.setOnClickListener(this);
        btnI.setOnClickListener(this);
        btnJ.setOnClickListener(this);
        btnK.setOnClickListener(this);
        btnL.setOnClickListener(this);
        btnM.setOnClickListener(this);
        btnN.setOnClickListener(this);
        btnO.setOnClickListener(this);
        btnP.setOnClickListener(this);
        btnQ.setOnClickListener(this);
        btnR.setOnClickListener(this);
        btnS.setOnClickListener(this);
        btnT.setOnClickListener(this);
        btnU.setOnClickListener(this);
        btnV.setOnClickListener(this);
        btnW.setOnClickListener(this);
        btnX.setOnClickListener(this);
        btnY.setOnClickListener(this);
        btnZ.setOnClickListener(this);


        LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        email = LoginActivity.prefs.getString("email", null);
        token = LoginActivity.prefs.getString("token", null);
        if (email != null && token != null) {
            showpDialog();
            if (wifiChecker() == true) {
                getGlossary(urlGlossary + email + urlToken + token);
            } else {
                glossaryModelList = new ArrayList<GlossaryModel>();
                glossaryModelList = DatabaseHandler.getInstance(GlossaryActivity.this).getGlossary();
                hidepDialog();
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryModelList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
            }
            showtAlphabet(glossaryModelList);
        }
    }
    private void showtAlphabet(List<GlossaryModel> list){
        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i).getAlphabet()) {
                case "A":
                    btnA.setVisibility(View.VISIBLE);
                    break;
                case "a":
                    btnA.setVisibility(View.VISIBLE);
                    break;
                case "B":
                    btnB.setVisibility(View.VISIBLE);
                    break;
                case "b":
                    btnB.setVisibility(View.VISIBLE);
                    break;
                case "C":
                    btnC.setVisibility(View.VISIBLE);
                    break;
                case "c":
                    btnC.setVisibility(View.VISIBLE);
                    break;
                case "D":
                    btnD.setVisibility(View.VISIBLE);
                    break;
                case "d":
                    btnD.setVisibility(View.VISIBLE);
                    break;
                case "E":
                    btnE.setVisibility(View.VISIBLE);
                    break;
                case "e":
                    btnE.setVisibility(View.VISIBLE);
                    break;
                case "F":
                    btnF.setVisibility(View.VISIBLE);
                    break;
                case "f":
                    btnF.setVisibility(View.VISIBLE);
                    break;
                case "G":
                    btnG.setVisibility(View.VISIBLE);
                    break;
                case "g":
                    btnG.setVisibility(View.VISIBLE);
                    break;
                case "H":
                    btnH.setVisibility(View.VISIBLE);
                    break;
                case "h":
                    btnH.setVisibility(View.VISIBLE);
                    break;
                case "I":
                    btnI.setVisibility(View.VISIBLE);
                    break;
                case "i":
                    btnI.setVisibility(View.VISIBLE);
                    break;
                case "J":
                    btnJ.setVisibility(View.VISIBLE);
                    break;
                case "j":
                    btnJ.setVisibility(View.VISIBLE);
                    break;
                case "K":
                    btnK.setVisibility(View.VISIBLE);
                    break;
                case "k":
                    btnK.setVisibility(View.VISIBLE);
                    break;
                case "L":
                    btnL.setVisibility(View.VISIBLE);
                    break;
                case "l":
                    btnL.setVisibility(View.VISIBLE);
                    break;
                case "M":
                    btnM.setVisibility(View.VISIBLE);
                    break;
                case "m":
                    btnM.setVisibility(View.VISIBLE);
                    break;
                case "N":
                    btnN.setVisibility(View.VISIBLE);
                    break;
                case "n":
                    btnN.setVisibility(View.VISIBLE);
                    break;
                case "O":
                    btnO.setVisibility(View.VISIBLE);
                    break;
                case "o":
                    btnO.setVisibility(View.VISIBLE);
                    break;
                case "P":
                    btnP.setVisibility(View.VISIBLE);
                    break;
                case "p":
                    btnP.setVisibility(View.VISIBLE);
                    break;
                case "Q":
                    btnQ.setVisibility(View.VISIBLE);
                    break;
                case "q":
                    btnQ.setVisibility(View.VISIBLE);
                    break;
                case "R":
                    btnR.setVisibility(View.VISIBLE);
                    break;
                case "r":
                    btnR.setVisibility(View.VISIBLE);
                    break;
                case "S":
                    btnS.setVisibility(View.VISIBLE);
                    break;
                case "s":
                    btnS.setVisibility(View.VISIBLE);
                    break;
                case "T":
                    btnT.setVisibility(View.VISIBLE);
                    break;
                case "t":
                    btnT.setVisibility(View.VISIBLE);
                    break;
                case "U":
                    btnU.setVisibility(View.VISIBLE);
                    break;
                case "u":
                    btnU.setVisibility(View.VISIBLE);
                    break;
                case "V":
                    btnV.setVisibility(View.VISIBLE);
                    break;
                case "v":
                    btnV.setVisibility(View.VISIBLE);
                    break;
                case "W":
                    btnW.setVisibility(View.VISIBLE);
                    break;
                case "X":
                    btnX.setVisibility(View.VISIBLE);
                    break;
                case "x":
                    btnX.setVisibility(View.VISIBLE);
                    break;
                case "Y":
                    btnY.setVisibility(View.VISIBLE);
                    break;
                case "y":
                    btnY.setVisibility(View.VISIBLE);
                    break;
                case "Z":
                    btnZ.setVisibility(View.VISIBLE);
                    break;
                case "z":
                    btnZ.setVisibility(View.VISIBLE);
                    break;

            }
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void getGlossary(String url) {
        showpDialog();

        final JsonArrayRequest jsonObjReq1 = new
                JsonArrayRequest(url, // DICDUXJHSNGHAJRSZNNJVQQP  admin@gmail.com
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", response.toString());
                        try {
                            GlossaryModel glossaryModel;
                            for (int i = 0; i < response.length(); i++) {
                                AddFilesModel addFiles = new AddFilesModel();
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int glossaryId = jsonObject.getInt("GlossaryId");
                                String alphabet = jsonObject.getString("Alphabet");
                                String word = jsonObject.getString("Word");
                                String definition = jsonObject.getString("Definition");
                                String file = jsonObject.getString("File");

                                glossaryModel = new GlossaryModel();
                                glossaryModel.setGlossaryId(glossaryId);
                                glossaryModel.setAlphabet(alphabet);
                                glossaryModel.setWord(word);
                                glossaryModel.setDefinition(definition);
                                glossaryModel.setFile(file);
                                glossaryModelList.add(glossaryModel);

                                int id = DatabaseHandler.getInstance(GlossaryActivity.this).getGlossary(glossaryId);
                                if (id == 0) {
                                    DatabaseHandler.getInstance(GlossaryActivity.this).addGlossary(glossaryModel);
                                }
                            }
                            hidepDialog();
                            mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryModelList, R.layout.custom_row_glossary, alphabet);
                            listView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            hidepDialog();
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.e("Error: ", error.getMessage());
                //showGlossary();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq1);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.send_email_layout:
                intent = new Intent(GlossaryActivity.this, SendEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.logo:
                intent = new Intent(GlossaryActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_layout:
                intent = new Intent(GlossaryActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_a:
                alphabet = "A";
                setBcakgroundColor();
                btnA.setBackgroundColor(Color.YELLOW);
                btnA.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("A")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_b:
                alphabet = "B";
                setBcakgroundColor();
                btnB.setBackgroundColor(Color.YELLOW);
                btnB.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("B")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_c:
                alphabet = "C";
                setBcakgroundColor();
                btnC.setBackgroundColor(Color.YELLOW);
                btnC.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("C")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_d:
                alphabet = "D";
                setBcakgroundColor();
                btnD.setBackgroundColor(Color.YELLOW);
                btnD.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("D")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_e:
                alphabet = "E";
                showpDialog();
                setBcakgroundColor();
                btnE.setBackgroundColor(Color.YELLOW);
                btnE.setTextColor(Color.BLUE);
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("E")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_f:
                alphabet = "F";
                setBcakgroundColor();
                btnF.setBackgroundColor(Color.YELLOW);
                btnF.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("F")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_g:
                alphabet = "G";
                setBcakgroundColor();
                btnG.setBackgroundColor(Color.YELLOW);
                btnG.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("G")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_h:
                alphabet = "H";
                setBcakgroundColor();
                btnH.setBackgroundColor(Color.YELLOW);
                btnH.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("H")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_i:
                alphabet = "I";
                setBcakgroundColor();
                btnI.setBackgroundColor(Color.YELLOW);
                btnI.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("I")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_j:
                alphabet = "J";
                setBcakgroundColor();
                btnJ.setBackgroundColor(Color.YELLOW);
                btnJ.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("J")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_k:
                alphabet = "K";
                setBcakgroundColor();
                btnK.setBackgroundColor(Color.YELLOW);
                btnK.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("K")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_l:
                alphabet = "L";
                setBcakgroundColor();
                btnL.setBackgroundColor(Color.YELLOW);
                btnL.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("L")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_m:
                alphabet = "M";
                setBcakgroundColor();
                btnM.setBackgroundColor(Color.YELLOW);
                btnM.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("M")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_n:
                alphabet = "N";
                setBcakgroundColor();
                btnN.setBackgroundColor(Color.YELLOW);
                btnN.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("N")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_o:
                alphabet = "O";
                setBcakgroundColor();
                btnO.setBackgroundColor(Color.YELLOW);
                btnO.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("O")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_p:
                alphabet = "P";
                setBcakgroundColor();
                btnP.setBackgroundColor(Color.YELLOW);
                btnP.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("P")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_q:
                alphabet = "Q";
                setBcakgroundColor();
                btnQ.setBackgroundColor(Color.YELLOW);
                btnQ.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("Q")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_r:
                alphabet = "R";
                setBcakgroundColor();
                btnR.setBackgroundColor(Color.YELLOW);
                btnR.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("R")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_s:
                alphabet = "S";
                setBcakgroundColor();
                btnS.setBackgroundColor(Color.YELLOW);
                btnS.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("S")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_t:
                alphabet = "T";
                setBcakgroundColor();
                btnT.setBackgroundColor(Color.YELLOW);
                btnT.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("T")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_u:
                alphabet = "U";
                setBcakgroundColor();
                btnU.setBackgroundColor(Color.YELLOW);
                btnU.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("U")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_v:
                alphabet = "V";
                setBcakgroundColor();
                btnV.setBackgroundColor(Color.YELLOW);
                btnV.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("V")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_w:
                alphabet = "W";
                setBcakgroundColor();
                btnW.setBackgroundColor(Color.YELLOW);
                btnW.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("W")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_x:
                alphabet = "X";
                setBcakgroundColor();
                btnX.setBackgroundColor(Color.YELLOW);
                btnX.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("X")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_y:
                alphabet = "y";
                setBcakgroundColor();
                btnY.setBackgroundColor(Color.YELLOW);
                btnY.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("Y")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
            case R.id.btn_z:
                alphabet = "Z";
                setBcakgroundColor();
                btnZ.setBackgroundColor(Color.YELLOW);
                btnZ.setTextColor(Color.BLUE);
                showpDialog();
                glossaryList = new ArrayList<GlossaryModel>();
                for (int i = 0; i < glossaryModelList.size(); i++) {
                    glossaryModel = new GlossaryModel();
                    glossaryModel = glossaryModelList.get(i);
                    if (glossaryModel.getAlphabet().equals("Z")) {
                        glossaryList.add(glossaryModel);
                    }
                }
                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryList, R.layout.custom_row_glossary, alphabet);
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hidepDialog();
                break;
//            case R.id.iv_zero_to_nine:
//                alphabet = "zero_to_nine";
//                mAdapter = new GlossaryAdapter(GlossaryActivity.this, glossaryModelList, R.layout.custom_row_glossary,alphabet);
//                listView.setAdapter(mAdapter);
//                break;
        }
    }

    private void setBcakgroundColor() {
        btnA.setBackgroundColor(Color.WHITE);
        btnA.setTextColor(Color.GRAY);
        btnB.setBackgroundColor(Color.WHITE);
        btnB.setTextColor(Color.GRAY);
        btnC.setBackgroundColor(Color.WHITE);
        btnC.setTextColor(Color.GRAY);
        btnD.setBackgroundColor(Color.WHITE);
        btnD.setTextColor(Color.GRAY);
        btnE.setBackgroundColor(Color.WHITE);
        btnE.setTextColor(Color.GRAY);
        btnF.setBackgroundColor(Color.WHITE);
        btnF.setTextColor(Color.GRAY);
        btnG.setBackgroundColor(Color.WHITE);
        btnG.setTextColor(Color.GRAY);
        btnH.setBackgroundColor(Color.WHITE);
        btnH.setTextColor(Color.GRAY);
        btnI.setBackgroundColor(Color.WHITE);
        btnI.setTextColor(Color.GRAY);
        btnJ.setBackgroundColor(Color.WHITE);
        btnJ.setTextColor(Color.GRAY);
        btnK.setBackgroundColor(Color.WHITE);
        btnK.setTextColor(Color.GRAY);
        btnL.setBackgroundColor(Color.WHITE);
        btnL.setTextColor(Color.GRAY);
        btnM.setBackgroundColor(Color.WHITE);
        btnM.setTextColor(Color.GRAY);
        btnN.setBackgroundColor(Color.WHITE);
        btnN.setTextColor(Color.GRAY);
        btnO.setBackgroundColor(Color.WHITE);
        btnO.setTextColor(Color.GRAY);
        btnP.setBackgroundColor(Color.WHITE);
        btnP.setTextColor(Color.GRAY);
        btnQ.setBackgroundColor(Color.WHITE);
        btnQ.setTextColor(Color.GRAY);
        btnR.setBackgroundColor(Color.WHITE);
        btnR.setTextColor(Color.GRAY);
        btnS.setBackgroundColor(Color.WHITE);
        btnS.setTextColor(Color.GRAY);
        btnT.setBackgroundColor(Color.WHITE);
        btnT.setTextColor(Color.GRAY);
        btnU.setBackgroundColor(Color.WHITE);
        btnU.setTextColor(Color.GRAY);
        btnV.setBackgroundColor(Color.WHITE);
        btnV.setTextColor(Color.GRAY);
        btnW.setBackgroundColor(Color.WHITE);
        btnW.setTextColor(Color.GRAY);
        btnX.setBackgroundColor(Color.WHITE);
        btnX.setTextColor(Color.GRAY);
        btnY.setBackgroundColor(Color.WHITE);
        btnY.setTextColor(Color.GRAY);
        btnZ.setBackgroundColor(Color.WHITE);
        btnZ.setTextColor(Color.GRAY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GlossaryActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
