package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.EmailSelectedTemp;
import com.ingentive.nahad.activeandroid.TocChildrenModel;
import com.ingentive.nahad.activeandroid.TocParentModel;
import com.ingentive.nahad.activeandroid.TocSubChildrenModel;
import com.ingentive.nahad.adapter.ContentsAdapter;
import com.ingentive.nahad.adapter.TableOfContentsAdapter;
import com.ingentive.nahad.adapter.TocAdapter;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.DatabaseHandler;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ContentsActivity extends Activity {

    private TextView tvContentsHeader;
    private ExpandableListView expandableListView;
    private View headerLayout;
    private RelativeLayout headerEmailLayout, headerMenuLayout;
    private int fileId = 0;
    private ProgressDialog pDialog;
    public static ContentsActivity contentsActivity;
    private String bookName = "",bookTitle = "";
    private ImageView ivLogo;
    private List<TocParentModel> parentModelList = new ArrayList<TocParentModel>();
    private List<TocChildrenModel> tocchildrenModels = new ArrayList<TocChildrenModel>();
    private List<TocSubChildrenModel> subChildrenModels = new ArrayList<TocSubChildrenModel>();
    private List<TocParentModel> plList = new ArrayList<TocParentModel>();
    private List<TocChildrenModel> clList = new ArrayList<TocChildrenModel>();
    private TocParentModel parentModel;
    private TocChildrenModel tocChildrenModel;
    private TocAdapter mAdapter;
    private ImageView ivEmail,ivMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        contentsActivity = ContentsActivity.this;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        initialize();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        Intent intent = new Intent(ContentsActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();

                        break;
                    }
                }

                return true;
            }
        });

        ivEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        List<EmailSelectedTemp>emilList =new ArrayList<EmailSelectedTemp>();
                        emilList=new Select().all().from(EmailSelectedTemp.class).where("file_id=?",fileId).execute();
                        if (emilList.size() == 0) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentsActivity.this);
                            alertDialog.setMessage("You must select at least 1 page!");

                            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }else {
                            Intent intent = new Intent(ContentsActivity.this, SendEmailActivity.class);
                            intent.putExtra("book_name", bookName);
                            intent.putExtra("file_id", fileId);
                            intent.putExtra("book_title", bookTitle);
                            intent.putExtra("from","contents");
                            startActivity(intent);
                        }

                        break;
                    }
                }

                return true;
            }
        });
        ivMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();

                        Intent  intent = new Intent(ContentsActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
    }

    protected void initialize() {
        ivEmail=(ImageView)findViewById(R.id.at_the_rate);
        ivMenu=(ImageView)findViewById(R.id.iv_menu_icon);
        tvContentsHeader = (TextView) findViewById(R.id.tv_contents_header);
        expandableListView = (ExpandableListView) findViewById(R.id.exp_lv);
        ivLogo = (ImageView) findViewById(R.id.logo);
        headerLayout = findViewById(R.id.header_layout_contects);
        headerEmailLayout = (RelativeLayout) headerLayout.findViewById(R.id.send_email_layout);
        headerMenuLayout = (RelativeLayout) headerLayout.findViewById(R.id.menu_layout);

       // headerMenuLayout.setOnClickListener(this);
        //headerEmailLayout.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null&&bundle.getString("book_name") != null && bundle.getString("book_title") != null) {
            bookName = bundle.getString("book_name");
            fileId = bundle.getInt("file_id");
            bookTitle = bundle.getString("book_title");
            tvContentsHeader.setText("" + bookTitle);
        }
        showContents();
    }

    public List<TocChildrenModel> getChildren(int parentId) {
        List<TocChildrenModel> childrenList = new ArrayList<TocChildrenModel>();
        childrenList = new Select().from(TocChildrenModel.class).where("parent_id=?", parentId).execute();
        return childrenList;
    }

    public List<TocSubChildrenModel> getSubChildren(int parentId) {
        List<TocSubChildrenModel> sub_childrenList = new ArrayList<TocSubChildrenModel>();
        sub_childrenList = new Select().from(TocSubChildrenModel.class).where("parent_id=?", parentId).execute();
        return sub_childrenList;
    }

    private void showContents() {
        parentModelList = new Select().all().from(TocParentModel.class).where("file_id=?", fileId).execute();

        if (parentModelList.size() > 0) {
            plList = new ArrayList<TocParentModel>();
            for (int i = 0; i < parentModelList.size(); i++) {
                parentModel = new TocParentModel();
                parentModel = parentModelList.get(i);
                tocchildrenModels = new ArrayList<TocChildrenModel>();
                clList = new ArrayList<TocChildrenModel>();
                tocchildrenModels = getChildren(parentModel.getTopicId());
                if (tocchildrenModels.size() > 0) {
                    for (int j = 0; j < tocchildrenModels.size(); j++) {
                        tocChildrenModel = new TocChildrenModel();
                        tocChildrenModel = tocchildrenModels.get(j);
                        subChildrenModels = new ArrayList<TocSubChildrenModel>();
                        subChildrenModels = getSubChildren(tocChildrenModel.getTopicId());
                        if (subChildrenModels.size() > 0) {
                            tocChildrenModel.tocSubChildrenArray = subChildrenModels;
                        }
                        clList.add(tocChildrenModel);
                    }
                }
                parentModel.tocChildrenAray = clList;
                plList.add(parentModel);
                clList = new ArrayList<TocChildrenModel>();

            }
            if (plList.size() > 0) {
                mAdapter = new TocAdapter(getApplication(), plList);
                expandableListView.setAdapter(mAdapter);

                expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        return false; // This way the expander cannot be collapsed
                    }
                });
            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        com.ingentive.nahad.activeandroid.AddFilesModel addFilesModel = new com.ingentive.nahad.activeandroid.AddFilesModel();
        addFilesModel = new Select().from(com.ingentive.nahad.activeandroid.AddFilesModel.class).orderBy("update_date DESC").executeSingle();
        if(addFilesModel!=null){
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
                if (hours-24> 0) {
                    Intent i = new Intent(ContentsActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }
}
