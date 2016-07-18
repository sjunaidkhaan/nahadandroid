package com.ingentive.nahad.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.artifex.mupdfdemo.MuPDFCore;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.AddFilesModel;
import com.ingentive.nahad.activeandroid.EmailSelectedTemp;
import com.ingentive.nahad.activeandroid.SendEmailModel;
import com.ingentive.nahad.adapter.BookInsidEmailGridViewAdapter;
import com.ingentive.nahad.adapter.TocEmailGridViewAdapter;
import com.ingentive.nahad.common.NetworkChangeReceiver;
import com.ingentive.nahad.model.GridViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendEmailActivity extends Activity {

    private GridView gridView;
    private BookInsidEmailGridViewAdapter gridAdapter;
    private Bitmap bitmap;
    private GridViewModel imageItem;
    private List<GridViewModel> mList = new ArrayList<GridViewModel>();
    private String bookName = "";
    private int fileId = 0;
    private String bookTitle = "";
    private List<GridViewModel> imageItemList;
    private TextView tvDone;
    private Button btnSend;
    private EditText etEmail, etSubject, etBody, etName;
    //    private String urlSendEmail = "http://pdfcms.azurewebsites.net/api/files/sendemail/?email=";
    private String urlSendEmail = "http://nahad.systemsinteractive.ca/api/files/sendemail/?email=";
    private String email;
    private String token;
    private String urlToken = "&token=";
    private ProgressDialog pDialog;
    private int conn = 0;
    private MuPDFCore core;
    private String mFilePath;
    private String emailStr = "";
    private String subjectStr = "";
    private String nameStr = "";
    private String bodyStr = "";
    private String jsonStr;
    private String callFrom = "";
    List<EmailSelectedTemp> selectedEmailList;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        gridView = (GridView) findViewById(R.id.gridview);
        btnSend = (Button) findViewById(R.id.btn_send);
        tvDone = (TextView) findViewById(R.id.tv_done);
        etBody = (EditText) findViewById(R.id.et_body);
        etEmail = (EditText) findViewById(R.id.et_email);
        etName = (EditText) findViewById(R.id.et_name);
        etSubject = (EditText) findViewById(R.id.et_subject);

        etBody.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etEmail.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etName.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        etSubject.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendEmailActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // List<AddFilesModel>list=DatabaseHandler.getInstance(SendEmailActivity.this).getAllFiles();
                if (etEmail.getText().toString().trim().replace(" ", "").length() > 0
                        && etSubject.getText().toString().trim().replace(" ", "").length() > 0
                        && etName.getText().toString().trim().replace(" ", "").length() > 0) {

                    LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
                    email = LoginActivity.prefs.getString("email", null);
                    token = LoginActivity.prefs.getString("token", null);
                    if (email != null && token != null) {
                        //showpDialog();
                        conn = NetworkChangeReceiver.getConnectivityStatus(SendEmailActivity.this);
                        if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                            ///sendEmail(urlSendEmail + email + urlToken + token);
                            emailStr = etEmail.getText().toString().trim();
                            subjectStr = etSubject.getText().toString().trim();
                            nameStr = etName.getText().toString().trim();
                            bodyStr = etBody.getText().toString().trim();
                            //new sendEmil(urlSendEmail + email + urlToken + token).execute();
                            if (callFrom.equals("book_inside")) {
                                sendEmailFromActivity(urlSendEmail + email + urlToken + token);
                            } else {
                                sendEmailFromAdapter(urlSendEmail + email + urlToken + token);
                                //new sendFromadapterEmil(urlSendEmail + email + urlToken + token).execute();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(SendEmailActivity.this, "Input field empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getInt("file_id") != 0 && bundle.getString("book_name") != null) {
            bookName = bundle.getString("book_name");
            fileId = bundle.getInt("file_id");
            bookTitle = bundle.getString("book_title");
            callFrom = bundle.getString("from");
            imageItemList = new ArrayList<GridViewModel>();
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "NAHAD_PDF" + File.separator + bookName;
            core = openFile(Uri.decode(mFilePath));
            if (core != null && core.countPages() == 0) {
                core = null;
            }
            if (core == null || core.countPages() == 0 || core.countPages() == -1) {
                Log.e("", "Document Not Opening");
            }
            if (core != null) {
                if (callFrom.equals("book_inside")) {
                    List<SendEmailModel> emailModelList = new ArrayList<SendEmailModel>();
                    emailModelList = new Select().all().from(SendEmailModel.class).where("file_id=?", fileId).orderBy("page_no ASC").execute();
                    for (int i = 0; i < emailModelList.size(); i++) {
                        PointF pointF = core.getPageSize(1);
                        bitmap = core.drawPage(emailModelList.get(i).getPageNo(), (int) 200, (int) 200, 0, 0, (int) 200, (int) 200);
                        imageItem = new GridViewModel();
                        imageItem.setImage(bitmap);
                        imageItem.setPageNo(emailModelList.get(i).getPageNo());
                        imageItem.setFile_id(emailModelList.get(i).getFileId());
                        imageItem.setBookName(bookName);
                        imageItem.setBook_title(bookTitle);
                        mList.add(imageItem);
                    }
                    gridAdapter = new BookInsidEmailGridViewAdapter(this, R.layout.grid_item_layout, mList);
                    gridView.setAdapter(gridAdapter);
                } else if (fileId != 0) {
                    selectedEmailList = new ArrayList<EmailSelectedTemp>();
                    selectedEmailList = new Select().all().from(EmailSelectedTemp.class).where("file_id=?", fileId).orderBy("page_no ASC").execute();
                    for (int i = 0; i < selectedEmailList.size(); i++) {
                        PointF pointF = core.getPageSize(1);
                        bitmap = core.drawPage(selectedEmailList.get(i).getPageNo(), (int) 200, (int) 200, 0, 0, (int) 200, (int) 200);
                        imageItem = new GridViewModel();
                        imageItem.setImage(bitmap);
                        imageItem.setPageNo(selectedEmailList.get(i).getPageNo());
                        imageItem.setFile_id(selectedEmailList.get(i).getFileId());
                        imageItem.setBookName(bookName);
                        imageItem.setBook_title(bookTitle);
                        mList.add(imageItem);
                    }
                    TocEmailGridViewAdapter dAdapter = new TocEmailGridViewAdapter(this, R.layout.grid_item_layout, mList);
                    gridView.setAdapter(dAdapter);
                }
            }
        }
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(SendEmailActivity.this, path);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("Adapter", e.getMessage());
            return null;
        }
        return core;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void sendEmailFromActivity(String url) {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidepDialog();
                        try {
                            Toast.makeText(SendEmailActivity.this, "" + response, Toast.LENGTH_LONG).show();
                            new Delete().from(SendEmailModel.class).where("file_id=?", fileId).execute();
//                            Intent i = new Intent(SendEmailActivity.this, BookInsideActivity.class);
//                            startActivity(i);
                            finish();
                            Log.d("", "" + response);

                        } catch (Exception e) {
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        String errorStr = error.getMessage();
                        Toast.makeText(SendEmailActivity.this, "errorStr: " + errorStr, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                AddFilesModel model = new Select().from(AddFilesModel.class).where("file_id=?", fileId).executeSingle();
                params.put("FileId", model.getFileId() + "");
                params.put("Version", model.getFileVersion() + "");
                params.put("emailTo", emailStr);
                List<SendEmailModel> intList = new ArrayList<SendEmailModel>();
                intList = new Select().all().from(SendEmailModel.class).where("file_id=?", fileId).orderBy("page_no ASC").execute();
                for (int i = 0; i < intList.size(); i++) {
                    params.put("pages[" + i + "]", intList.get(i).getPageNo() + 1 + "");
                }
                params.put("subject", subjectStr);
                params.put("body", bodyStr);
                params.put("name", nameStr);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sendEmailFromAdapter(String url) {
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidepDialog();
                        try {
                            Toast.makeText(SendEmailActivity.this, "" + response, Toast.LENGTH_LONG).show();
                            new Delete().from(EmailSelectedTemp.class).where("file_id=?", fileId).execute();
                            finish();
                            Log.d("", "" + response);

                        } catch (Exception e) {
                            hidepDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        String errorStr = error.getMessage();
                        Toast.makeText(SendEmailActivity.this, "errorStr: " + errorStr, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                AddFilesModel model = new Select().from(AddFilesModel.class).where("file_id=?", fileId).executeSingle();
                params.put("FileId", model.getFileId() + "");
                params.put("Version", model.getFileVersion() + "");
                params.put("emailTo", emailStr);
//                List<SendEmailModel> intList=new ArrayList<SendEmailModel>();
//                intList=new Select().all().from(SendEmailModel.class).where("file_id=?",fileId).execute();

                for (int i = 0; i < selectedEmailList.size(); i++) {
                    params.put("pages[" + i + "]", selectedEmailList.get(i).getPageNo() + "");
                }
                params.put("subject", subjectStr);
                params.put("body", bodyStr);
                params.put("name", nameStr);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        AddFilesModel addFilesModel = new AddFilesModel();
        addFilesModel = new Select().from(AddFilesModel.class).orderBy("update_date DESC").executeSingle();
        if (addFilesModel != null) {
            String toyBornTime = addFilesModel.getUpdateDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date oldDate = dateFormat.parse(toyBornTime);
                System.out.println(oldDate);
                Date currentDate = new Date();
                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                //long days = hours / 24;
                if (hours - 24 > 0) {
                    Intent i = new Intent(SendEmailActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
