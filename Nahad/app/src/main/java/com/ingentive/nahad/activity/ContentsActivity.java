package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.ContentsAdapter;
import com.ingentive.nahad.adapter.TableOfContentsAdapter;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.DatabaseHandler;
import com.ingentive.nahad.common.JSONRequests;
import com.ingentive.nahad.common.ServiceHandler;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.ContentsChildModel;
import com.ingentive.nahad.model.ContentsParentModel;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContentsActivity extends Activity implements View.OnClickListener {

    private TextView tvContentsHeader;
    private ExpandableListView expandableListView;
    private ArrayList<ContentsParentModel> deptList = new ArrayList<ContentsParentModel>();
    TableOfContentsAdapter adapter;
    List<TableOfContentsModel> parentModelsList = new ArrayList<TableOfContentsModel>();
    TableOfContentsModel tableOfContentsModel;
    TableOfContentsFileModel tableOfContentsFileModel;
    TableOfContentsChildrenModel tableOfContentsChildrenModel;
    TableOfContentsSubChildrenModel tableOfContentsSubChildrenModel;
    private File[] filelist;
    private View headerLayout;
    private RelativeLayout headerEmailLayout, headerMenuLayout;
    private String urlToken = "&token=";
    private String email;
    private String token;
    private boolean is3g;
    private boolean isWifi;
    private String urlTableOfContents = "http://pdfcms.azurewebsites.net/api/tocs/get/";
    //http://pdfcms.azurewebsites.net/api/tocs/get/25?email=admin@gmail.COM&token=BNSRUBAHHWTSCKMUUOEOFPMC
    List<TableOfContentsModel> tableOfContentsModelList;
    List<TableOfContentsFileModel> tableOfContentsFileModelList = new ArrayList<TableOfContentsFileModel>();
    List<TableOfContentsChildrenModel> tableOfContentsChildrenModelList = new ArrayList<TableOfContentsChildrenModel>();
    List<TableOfContentsSubChildrenModel> tableOfContentsSubChildrenModelList = new ArrayList<TableOfContentsSubChildrenModel>();

    List<TableOfContentsFileModel> fileChildList = new ArrayList<TableOfContentsFileModel>();
    List<TableOfContentsChildrenModel> childrenList = new ArrayList<TableOfContentsChildrenModel>();
    List<TableOfContentsSubChildrenModel> subChildrenList = new ArrayList<TableOfContentsSubChildrenModel>();

    int fileId = 0;
    private ProgressDialog pDialog;
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    DefaultHttpClient httpClient;
    HttpEntity httpEntity = null;
    HttpResponse httpResponse = null;
    public static ContentsActivity contentsActivity;
    String bookName = "";
    String bookTitle = "";
    ImageView ivLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        contentsActivity = ContentsActivity.this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        initialize();

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    protected void initialize() {
        tvContentsHeader = (TextView) findViewById(R.id.tv_contents_header);
        expandableListView = (ExpandableListView) findViewById(R.id.exp_lv);
        ivLogo = (ImageView) findViewById(R.id.logo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentsActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        headerLayout = findViewById(R.id.header_layout_contects);
        headerEmailLayout = (RelativeLayout) headerLayout.findViewById(R.id.send_email_layout);
        headerMenuLayout = (RelativeLayout) headerLayout.findViewById(R.id.menu_layout);

        headerMenuLayout.setOnClickListener(this);
        headerEmailLayout.setOnClickListener(this);

//        Intent iin = getIntent();
//        Bundle b = iin.getExtras();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
            bookName = bundle.getString("book_name");
            fileId = bundle.getInt("file_id");
            bookTitle = bundle.getString("book_title");
            tvContentsHeader.setText("" + bookTitle);
        }

        LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        email = LoginActivity.prefs.getString("email", null);
        token = LoginActivity.prefs.getString("token", null);
        showContents();
    }

//    private void expandAll() {
//        for (int i = 0; i < adapter.getGroupCount(); i++)
//            expandableListView.expandGroup(i);
//    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.send_email_layout:

                intent = new Intent(ContentsActivity.this, SendEmailActivity.class);
                intent.putExtra("book_name", bookName);
                intent.putExtra("file_id", fileId);
                intent.putExtra("book_title", bookTitle);
                startActivity(intent);

                break;
            case R.id.menu_layout:
                intent = new Intent(ContentsActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showContents() {
        tableOfContentsModelList = new ArrayList<TableOfContentsModel>();
        List<TableOfContentsModel> mList = new ArrayList<TableOfContentsModel>();
        mList = DatabaseHandler.getInstance(ContentsActivity.this).getTableOfContentsModels(fileId);
        for (int i = 0; i < mList.size(); i++) {
            tableOfContentsModel = new TableOfContentsModel();
            tableOfContentsModel = mList.get(i);
            tableOfContentsFileModel = new TableOfContentsFileModel();
            tableOfContentsFileModel = DatabaseHandler.getInstance(ContentsActivity.this).getTableOfContentsFileModel(fileId);
            tableOfContentsModel.setTableOfContentsFileModel(tableOfContentsFileModel);
            tableOfContentsChildrenModelList = new ArrayList<TableOfContentsChildrenModel>();

            List<TableOfContentsChildrenModel> childrenModels = new ArrayList<TableOfContentsChildrenModel>();
            childrenModels = DatabaseHandler.getInstance(ContentsActivity.this)
                    .getTableOfContentsChildrenModels(tableOfContentsModel.getTopicId(), tableOfContentsModel.getFileId());

            for (int j = 0; j < childrenModels.size(); j++) {
                tableOfContentsChildrenModel = new TableOfContentsChildrenModel();
                tableOfContentsChildrenModel = childrenModels.get(j);

                tableOfContentsSubChildrenModelList = new ArrayList<TableOfContentsSubChildrenModel>();
                tableOfContentsSubChildrenModelList = DatabaseHandler.getInstance(ContentsActivity.this)
                        .getTableOfContentsSubChildrenModels(tableOfContentsChildrenModel.
                                getTopicId(), tableOfContentsChildrenModel.getFileId());

                tableOfContentsChildrenModel.setSubChildrendArray(tableOfContentsSubChildrenModelList);
                tableOfContentsChildrenModelList.add(tableOfContentsChildrenModel);
            }
            tableOfContentsModel.setChildrenArray(tableOfContentsChildrenModelList);
            tableOfContentsModelList.add(tableOfContentsModel);
        }
        if (tableOfContentsModelList.size() > 0) {
            adapter = new TableOfContentsAdapter(getApplication(), tableOfContentsModelList);
            expandableListView.setAdapter(adapter);

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    return false;
                }
            });
            //expandAll();
        }
    }
}
