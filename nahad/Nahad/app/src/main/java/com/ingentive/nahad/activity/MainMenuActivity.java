package com.ingentive.nahad.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ingentive.nahad.R;
import com.ingentive.nahad.activeandroid.AddFilesModel;
import com.ingentive.nahad.activeandroid.EmailSelectedTemp;
import com.ingentive.nahad.activeandroid.TocChildrenModel;
import com.ingentive.nahad.activeandroid.TocParentModel;
import com.ingentive.nahad.activeandroid.TocSubChildrenModel;
import com.ingentive.nahad.adapter.GlossaryAdapter;
import com.ingentive.nahad.adapter.MainMenuAdapter;
import com.ingentive.nahad.adapter.TocAdapter;
import com.ingentive.nahad.common.AppController;
import com.ingentive.nahad.common.DatabaseHandler;

import com.ingentive.nahad.common.NetworkChangeReceiver;
import com.ingentive.nahad.common.ServiceHandler;

import com.ingentive.nahad.model.FilesCategory;
import com.ingentive.nahad.model.GlossaryModel;
import com.ingentive.nahad.model.TableOfContentsChildrenModel;
import com.ingentive.nahad.model.TableOfContentsFileModel;
import com.ingentive.nahad.model.TableOfContentsModel;
import com.ingentive.nahad.model.TableOfContentsSubChildrenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainMenuActivity extends Activity implements View.OnClickListener {

    //private String urlGetAllFiles = "http://pdfcms.azurewebsites.net/api/files?email=&token=";
    private String urlAllFiles1 = "http://pdfcms.azurewebsites.net/api/files?email=";
    private String urlToken = "&token=";
    private String urlTableOfContents = "http://pdfcms.azurewebsites.net/api/tocs/get/";
    private ProgressDialog pDialog;
    private String email;
    private String token;
    private ProgressDialog mProgressDialog;
    private ProgressDialog pdialog;
    public static final int progress_bar_type = 0;
    private String folder_main = "NAHAD_PDF";
    private RelativeLayout tv_institute_handbook_layout, fabb_layout, resources_layout,
            white_papers_layout, glossary_lauout, visit_website_layout;//, right_layout;
    private ImageView iv_menu, ivLogo;
    private ListView listView;
    private MainMenuAdapter mAdapter;
    private TextView tvSelectedLayoutName;
    private Select select;
    private TocParentModel tocParentModel;
    private TocChildrenModel tocChildrenModel;
    private TocSubChildrenModel tocSubChildrenModel;
    private Intent book_intent, content_intent;
    private int conn = 0;
    private List<AddFilesModel> addFilesModelList;
    private List<Integer> fileIds;
    private List<AddFilesModel> particularList;
    private List<Integer> idsForToc;
    private List<Integer> idsForDowndload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        initialize();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void initialize() {
        ivLogo = (ImageView) findViewById(R.id.logo);
        tv_institute_handbook_layout = (RelativeLayout) findViewById(R.id.tv_institute_handbook_layout);
        fabb_layout = (RelativeLayout) findViewById(R.id.fabb_layout);
        resources_layout = (RelativeLayout) findViewById(R.id.resources_layout);
        white_papers_layout = (RelativeLayout) findViewById(R.id.white_papers_layout);
        glossary_lauout = (RelativeLayout) findViewById(R.id.glossary_lauout);
        visit_website_layout = (RelativeLayout) findViewById(R.id.visit_website_layout);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        listView = (ListView) findViewById(R.id.listview);
        tvSelectedLayoutName = (TextView) findViewById(R.id.tv_selected);

        tv_institute_handbook_layout.setOnClickListener(this);
        fabb_layout.setOnClickListener(this);
        resources_layout.setOnClickListener(this);
        white_papers_layout.setOnClickListener(this);
        glossary_lauout.setOnClickListener(this);
        visit_website_layout.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        //set the ontouch listener
        ivLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        ivLogo.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        ivLogo.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        ivLogo.getDrawable().clearColorFilter();
                        ivLogo.invalidate();
                        iv_menu.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        break;
                    }
                }

                return true;
            }
        });


        LoginActivity.prefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        email = LoginActivity.prefs.getString("email", null);
        token = LoginActivity.prefs.getString("token", null);
        if (email != null && token != null) {
            conn = NetworkChangeReceiver.getConnectivityStatus(MainMenuActivity.this);
            if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                Bundle bundle = getIntent().getExtras();

                if (bundle != null && bundle.getString("intent") != null) {
                    if (bundle.getString("intent").equals("splash_intent")) {
                        new getAllFiles(urlAllFiles1 + email + urlToken + token).execute();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo:
                iv_menu.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                break;
            case R.id.tv_institute_handbook_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Institute Handbook");
                getParticularData(1);
                break;
            case R.id.fabb_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Fabrication Guides");
                getParticularData(2);
                break;
            case R.id.resources_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("Resourcess");
                getParticularData(4);
                break;
            case R.id.white_papers_layout:
                tvSelectedLayoutName.setVisibility(View.VISIBLE);
                tvSelectedLayoutName.setText("White Papers");
                getParticularData(3);
                break;
            case R.id.glossary_lauout:
                Intent i = new Intent(MainMenuActivity.this, GlossaryActivity.class);
                startActivity(i);
                break;
            case R.id.visit_website_layout:
                conn = NetworkChangeReceiver.getConnectivityStatus(MainMenuActivity.this);
                if (conn == NetworkChangeReceiver.TYPE_MOBILE || conn == NetworkChangeReceiver.TYPE_WIFI) {
                    WebView mWebview = new WebView(this);
                    mWebview.loadUrl("http://www.nahad.org/aws/NAHAD/pt/sp/home_page");
                    setContentView(mWebview);
                } else {
                    Toast.makeText(getApplicationContext(), "Please make sure, your network connection is ON ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    public void getParticularData(int category) {
        particularList = new ArrayList<AddFilesModel>();
        select = new Select();
        particularList = select.all().from(AddFilesModel.class).where("category_id=?", category).execute();
        //particularList = DatabaseHandler.getInstance(MainMenuActivity.this).getParticularCategory(category);
        mAdapter = new MainMenuAdapter(MainMenuActivity.this, particularList, R.layout.custom_row_menu);
        //right_layout.setBackgroundColor(0xFFFFFFFF);
        iv_menu.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new Delete().from(EmailSelectedTemp.class).where("file_id=?", particularList.get(position).getFileId()).execute();
                book_intent = new Intent(MainMenuActivity.this, BookInsideActivity.class);
                book_intent.putExtra("book_name", particularList.get(position).getFileName());
                book_intent.putExtra("file_id", particularList.get(position).getFileId());
                book_intent.putExtra("book_title", particularList.get(position).getFileTitle());
                book_intent.putExtra("page_no", 0);

                content_intent = new Intent(MainMenuActivity.this, ContentsActivity.class);
                content_intent.putExtra("book_name", particularList.get(position).getFileName());
                content_intent.putExtra("file_id", particularList.get(position).getFileId());
                content_intent.putExtra("book_title", particularList.get(position).getFileTitle());
                List<TocParentModel> mList = new ArrayList<TocParentModel>();
                select = new Select();
                mList = select.all().from(TocParentModel.class).where("file_id=?", particularList.get(position).getFileId()).execute();
                if (mList.size() > 1) {
                    startActivity(content_intent);
                } else {
                    startActivity(book_intent);
                }
            }
        });
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pdialog = new ProgressDialog(this);
                pdialog.setIndeterminate(false);
                pdialog.setMax(100);
                pdialog.setMessage("Downloading file. Please wait...");
                pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pdialog.setCancelable(false);
                pdialog.show();
                return pdialog;
            default:
                return null;
        }
    }

    private class getAllFiles extends AsyncTask<Void, Void, Void> {
        String url = "";

        public getAllFiles(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            android.util.Log.d("Response: ", "> " + jsonStr);
            JSONObject jsonObject;
            AddFilesModel addFiles;
            addFilesModelList = new ArrayList<AddFilesModel>();
            fileIds = new ArrayList<Integer>();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    String name = jsonObject.getString("Name");
                    int categoryId = jsonObject.getInt("CategoryId");
                    int pagesLimit = jsonObject.getInt("pagesLimit");
                    int version = jsonObject.getInt("Version");
                    int Toc_Version = jsonObject.getInt("Toc_Version");
                    String title = jsonObject.getString("Title");
                    int fileld = jsonObject.getInt("FileId");
                    String path = jsonObject.getString("Path");

                  /*
                * Category Jsonbject
                 */
                    JSONObject category = jsonObject.getJSONObject("Category");
                    int Identifier = category.getInt("Identifier");
                    String Name = category.getString("Name");
                    int CategoryId = category.getInt("CategoryId");

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    // formattedDate have current date/time
                    // 2016-06-16 19
                    AddFilesModel addFilesModel = new AddFilesModel();
                    addFilesModel.fileName = name;
                    addFilesModel.categoryId = categoryId;
                    addFilesModel.pagesLimit = pagesLimit;
                    addFilesModel.fileVersion = version;
                    addFilesModel.tocVersion = Toc_Version;
                    addFilesModel.fileTitle = title;
                    addFilesModel.fileId = fileld;
                    addFilesModel.filePath = path;
                    addFilesModel.categoryIdentifier = Identifier;
                    addFilesModel.categoryName = Name;
                    addFilesModel.updateDate = formattedDate;

                    addFiles = new AddFilesModel();
                    addFiles = new Select().from(AddFilesModel.class).where("file_id=?", fileld).executeSingle();
                    if (addFiles == null) {
                        addFilesModelList.add(addFilesModel);
                        fileIds.add(fileld);
                    } else if (addFiles.getFileVersion() != version && addFiles.getTocVersion() != Toc_Version) {
                        addFiles.fileName = name;
                        addFiles.categoryId = categoryId;
                        addFiles.pagesLimit = pagesLimit;
                        addFiles.fileVersion = version;
                        addFiles.tocVersion = Toc_Version;
                        addFiles.fileTitle = title;
                        addFiles.fileId = fileld;
                        addFiles.filePath = path;
                        addFiles.categoryIdentifier = Identifier;
                        addFiles.categoryName = Name;
                        addFiles.updateDate = formattedDate;

                        addFilesModelList.add(addFiles);
                        fileIds.add(fileld);
                    } else if (addFiles.getFileVersion() == version && addFiles.getTocVersion() != Toc_Version) {
                        fileIds.add(fileld);
                    } else if (addFiles.getFileVersion() != version) {
                        addFiles.fileName = name;
                        addFiles.categoryId = categoryId;
                        addFiles.pagesLimit = pagesLimit;
                        addFiles.fileVersion = version;
                        addFiles.tocVersion = Toc_Version;
                        addFiles.fileTitle = title;
                        addFiles.fileId = fileld;
                        addFiles.filePath = path;
                        addFiles.categoryIdentifier = Identifier;
                        addFiles.categoryName = Name;
                        addFiles.updateDate = formattedDate;
                        addFilesModelList.add(addFiles);
                    } else {
                        addFiles.fileTitle = title;
                        addFiles.pagesLimit = pagesLimit;
                        addFiles.save();
                    }
                }
            } catch (JSONException e) {
                hidepDialog();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // hidepDialog();
            if (fileIds.size() > 0) {
                new getTableOfContents(fileIds).execute();
            }
            if (addFilesModelList.size() > 0) {
                new DownloadFileFromURL(addFilesModelList).execute();
            } else {
                hidepDialog();
            }
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        List<AddFilesModel> files;

        DownloadFileFromURL(List<AddFilesModel> files) {
            this.files = files;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hidepDialog();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            hidepDialog();
            for (int j = 0; j < files.size(); j++) {
                try {
                    URL url = new URL(files.get(j).getFilePath().replace(" ", "%20"));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    // connect
                    urlConnection.connect();
                    int lenghtOfFile = urlConnection.getContentLength();
                    File file = new File("/sdcard/" + folder_main + "/", files.get(j).fileName);
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    // Stream used for reading the data from the internet
                    InputStream inputStream = urlConnection.getInputStream();
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        fileOutput.write(data, 0, count);
                    }
                    fileOutput.flush();
                    fileOutput.close();
                    inputStream.close();
                    AddFilesModel model = new AddFilesModel();

                    files.get(j).save();
                } catch (final MalformedURLException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Toast.makeText(getApplication(), filesModel.getfTitle() + " not exist!", Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplication(), " not exist!", Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplication(), "Failed to download PDF. Please check your internet connection.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pdialog.setProgress(Integer.parseInt(progress[0]));
            pdialog.setMessage("Downloading file. Please wait...");
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
//            if (fileIds.size() > 0) {
//                new getTableOfContents(fileIds).execute();
//            }
        }
    }

    private class getTableOfContents extends AsyncTask<Void, Void, Void> {
        List<Integer> fileIds = new ArrayList<Integer>();
        private ProgressDialog progressDialog;
        private String url = "";
        ServiceHandler sh;
        String jsonStr;
        JSONObject jsonObject;

        public getTableOfContents(List<Integer> fileIds) {
            this.fileIds = fileIds;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            for (int i = 0; i < fileIds.size(); i++) {
                int fileid = fileIds.get(i);
                url = urlTableOfContents + fileIds.get(i) + "?email=" + email + urlToken + token;
                sh = new ServiceHandler();
                jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
                android.util.Log.d("Response: ", "> " + jsonStr);
                if (!jsonStr.equals("[]")) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonStr);
                        ///////////// chek update available
                        jsonObject = (JSONObject) jsonArray.get(0);
                        int file_id = jsonObject.getInt("FileId");
                        JSONObject fileobj = jsonObject.getJSONObject("File");
                        int tocVersion = fileobj.getInt("Toc_Version");
                        TocParentModel parentModel = new TocParentModel();
                        select = new Select();
                        parentModel = select.from(TocParentModel.class).where("file_id=?", file_id).executeSingle();
                        if (parentModel == null) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                jsonObject = (JSONObject) jsonArray.get(j);
                                tocParentModel = new TocParentModel();
                            /*
                            * TocParent Model
                             */
                                int topicId = jsonObject.getInt("TopicId");
                                String name = jsonObject.getString("Name");
                                int pageNo = jsonObject.getInt("PageNo");
                                String parentId = jsonObject.getString("ParentId");
                                if (parentId.equals("null") || parentId == null) {
                                    tocParentModel.parentId = 0;
                                } else {
                                    tocParentModel.parentId = Integer.parseInt(parentId.toString());
                                }
                                int fileId = jsonObject.getInt("FileId");

                                tocParentModel.topicId = topicId;
                                tocParentModel.name = name;
                                tocParentModel.pageNo = pageNo;
                                tocParentModel.fileId = fileId;

                                JSONObject fileObject = jsonObject.getJSONObject("File");
                                int file_fileId = fileObject.getInt("FileId");
                                String file_title = fileObject.getString("Title");
                                String file_name = fileObject.getString("Name");
                                String file_path = fileObject.getString("Path");
                                int file_version = fileObject.getInt("Version");
                                int toc_Version = fileObject.getInt("Toc_Version");
                                int file_categoryId = fileObject.getInt("CategoryId");
                                String file_category = fileObject.getString("Category");
                                int pagesLimit = fileObject.getInt("pagesLimit");
                                String file_categories = fileObject.getString("Categories");
                                String file_file = fileObject.getString("File");
                                tocParentModel.fileTitle = file_title;
                                tocParentModel.fileName = file_name;
                                tocParentModel.filePath = file_path;
                                tocParentModel.fileVersion = file_version;
                                tocParentModel.tocVersion = toc_Version;
                                tocParentModel.categoryId = file_categoryId;
                                tocParentModel.category = file_category;
                                tocParentModel.pagesLimit = pagesLimit;
                                tocParentModel.categories = file_categories;
                                tocParentModel.file = file_file;

                                JSONObject childObject;
                                JSONArray childrenArray = jsonObject.getJSONArray("Children");
                                for (int k = 0; k < childrenArray.length(); k++) {
                                    tocChildrenModel = new TocChildrenModel();
                                    childObject = childrenArray.getJSONObject(k);
                                    int child_topicId = childObject.getInt("TopicId");
                                    String child_name = childObject.getString("Name");
                                    int child_pageNo = childObject.getInt("PageNo");
                                    int child_parentId = childObject.getInt("ParentId");
                                    int child_fileId = childObject.getInt("FileId");
                                    String child_file = childObject.getString("File");

                                    tocChildrenModel.topicId = child_topicId;
                                    tocChildrenModel.name = child_name;
                                    tocChildrenModel.pageNo = child_pageNo;
                                    tocChildrenModel.parentId = child_parentId;
                                    tocChildrenModel.fileId = child_fileId;
                                    tocChildrenModel.file = child_file;

                                    JSONArray subChildrenArray = childObject.getJSONArray("Children");
                                    JSONObject subChildObject;
                                    for (int m = 0; m < subChildrenArray.length(); m++) {
                                        subChildObject = subChildrenArray.getJSONObject(m);
                                        tocSubChildrenModel = new TocSubChildrenModel();
                                        int sub_child_topicId = subChildObject.getInt("TopicId");
                                        String sub_child_name = subChildObject.getString("Name");
                                        int sub_child_pageNo = subChildObject.getInt("PageNo");
                                        int sub_child_parentId = subChildObject.getInt("ParentId");
                                        int sub_child_fileId = subChildObject.getInt("FileId");
                                        String sub_child_file = childObject.getString("File");

                                        tocSubChildrenModel.topicId = sub_child_topicId;
                                        tocSubChildrenModel.name = sub_child_name;
                                        tocSubChildrenModel.pageNo = sub_child_pageNo;
                                        tocSubChildrenModel.parentId = sub_child_parentId;
                                        tocSubChildrenModel.fileId = sub_child_fileId;
                                        tocSubChildrenModel.file = sub_child_file;
                                        tocSubChildrenModel.save();
                                    }
                                    tocChildrenModel.save();
                                }
                                tocParentModel.save();
                            }
                        } else if (parentModel.getTocVersion() != tocVersion) {
                            // parentModel = select.from(TocParentModel.class).where("file_id=?", file_id).executeSingle();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                jsonObject = (JSONObject) jsonArray.get(j);
                                //tocParentModel = new TocParentModel();
                            /*
                            * TocParent Model
                             */
                                int topicId = jsonObject.getInt("TopicId");
                                String name = jsonObject.getString("Name");
                                int pageNo = jsonObject.getInt("PageNo");
                                String parentId = jsonObject.getString("ParentId");
                                int parnt_id = 0;
                                if (parentId.equals("null") || parentId == null) {
                                    parentModel.parentId = 0;
                                    parnt_id = 0;
                                } else {
                                    parentModel.parentId = Integer.parseInt(parentId.toString());
                                    parnt_id = Integer.parseInt(parentId.toString());
                                }
                                int fileId = jsonObject.getInt("FileId");

                                parentModel.topicId = topicId;
                                parentModel.name = name;
                                parentModel.pageNo = pageNo;
                                parentModel.fileId = fileId;

                                JSONObject fileObject = jsonObject.getJSONObject("File");
                                int file_fileId = fileObject.getInt("FileId");
                                String file_title = fileObject.getString("Title");
                                String file_name = fileObject.getString("Name");
                                String file_path = fileObject.getString("Path");
                                int file_version = fileObject.getInt("Version");
                                int toc_Version = fileObject.getInt("Toc_Version");
                                int file_categoryId = fileObject.getInt("CategoryId");
                                String file_category = fileObject.getString("Category");
                                int pagesLimit = fileObject.getInt("pagesLimit");
                                String file_categories = fileObject.getString("Categories");
                                String file_file = fileObject.getString("File");
                                parentModel.fileTitle = file_title;
                                parentModel.fileName = file_name;
                                parentModel.filePath = file_path;
                                parentModel.fileVersion = file_version;
                                parentModel.tocVersion = toc_Version;
                                parentModel.categoryId = file_categoryId;
                                parentModel.category = file_category;
                                parentModel.pagesLimit = pagesLimit;
                                parentModel.categories = file_categories;
                                parentModel.file = file_file;

                                JSONObject childObject;
                                JSONArray childrenArray = jsonObject.getJSONArray("Children");
                                for (int k = 0; k < childrenArray.length(); k++) {
                                    TocChildrenModel tocChildModel = new TocChildrenModel();
                                    tocChildModel = new Select().from(TocChildrenModel.class).where("parent_id=?", parnt_id).executeSingle();
                                    if (tocChildModel != null) {
                                        childObject = childrenArray.getJSONObject(k);
                                        int child_topicId = childObject.getInt("TopicId");
                                        String child_name = childObject.getString("Name");
                                        int child_pageNo = childObject.getInt("PageNo");
                                        int child_parentId = childObject.getInt("ParentId");
                                        int child_fileId = childObject.getInt("FileId");
                                        String child_file = childObject.getString("File");

                                        tocChildModel.topicId = child_topicId;
                                        tocChildModel.name = child_name;
                                        tocChildModel.pageNo = child_pageNo;
                                        tocChildModel.parentId = child_parentId;
                                        tocChildModel.fileId = child_fileId;
                                        tocChildModel.file = child_file;

                                        JSONArray subChildrenArray = childObject.getJSONArray("Children");
                                        JSONObject subChildObject;
                                        for (int m = 0; m < subChildrenArray.length(); m++) {
                                            TocSubChildrenModel sub_childrenModel = new TocSubChildrenModel();
                                            sub_childrenModel = new Select().from(TocSubChildrenModel.class).where("parent_id=?", child_topicId).executeSingle();
                                            if (sub_childrenModel != null) {
                                                subChildObject = subChildrenArray.getJSONObject(m);
                                                int sub_child_topicId = subChildObject.getInt("TopicId");
                                                String sub_child_name = subChildObject.getString("Name");
                                                int sub_child_pageNo = subChildObject.getInt("PageNo");
                                                int sub_child_parentId = subChildObject.getInt("ParentId");
                                                int sub_child_fileId = subChildObject.getInt("FileId");
                                                String sub_child_file = childObject.getString("File");

                                                sub_childrenModel.topicId = sub_child_topicId;
                                                sub_childrenModel.name = sub_child_name;
                                                sub_childrenModel.pageNo = sub_child_pageNo;
                                                sub_childrenModel.parentId = sub_child_parentId;
                                                sub_childrenModel.fileId = sub_child_fileId;
                                                sub_childrenModel.file = sub_child_file;
                                                sub_childrenModel.save();
                                            } else {
                                                subChildObject = subChildrenArray.getJSONObject(m);
                                                tocSubChildrenModel = new TocSubChildrenModel();
                                                int sub_child_topicId = subChildObject.getInt("TopicId");
                                                String sub_child_name = subChildObject.getString("Name");
                                                int sub_child_pageNo = subChildObject.getInt("PageNo");
                                                int sub_child_parentId = subChildObject.getInt("ParentId");
                                                int sub_child_fileId = subChildObject.getInt("FileId");
                                                String sub_child_file = childObject.getString("File");

                                                tocSubChildrenModel.topicId = sub_child_topicId;
                                                tocSubChildrenModel.name = sub_child_name;
                                                tocSubChildrenModel.pageNo = sub_child_pageNo;
                                                tocSubChildrenModel.parentId = sub_child_parentId;
                                                tocSubChildrenModel.fileId = sub_child_fileId;
                                                tocSubChildrenModel.file = sub_child_file;
                                                tocSubChildrenModel.save();
                                            }
                                        }
                                        tocChildModel.save();
                                    } else {
                                        tocChildrenModel = new TocChildrenModel();
                                        childObject = childrenArray.getJSONObject(k);
                                        int child_topicId = childObject.getInt("TopicId");
                                        String child_name = childObject.getString("Name");
                                        int child_pageNo = childObject.getInt("PageNo");
                                        int child_parentId = childObject.getInt("ParentId");
                                        int child_fileId = childObject.getInt("FileId");
                                        String child_file = childObject.getString("File");

                                        tocChildrenModel.topicId = child_topicId;
                                        tocChildrenModel.name = child_name;
                                        tocChildrenModel.pageNo = child_pageNo;
                                        tocChildrenModel.parentId = child_parentId;
                                        tocChildrenModel.fileId = child_fileId;
                                        tocChildrenModel.file = child_file;

                                        JSONArray subChildrenArray = childObject.getJSONArray("Children");
                                        JSONObject subChildObject;
                                        for (int m = 0; m < subChildrenArray.length(); m++) {
                                            TocSubChildrenModel sub_childrenModel = new TocSubChildrenModel();
                                            sub_childrenModel = new Select().from(TocSubChildrenModel.class).where("parent_id=?", child_topicId).executeSingle();
                                            if (sub_childrenModel != null) {
                                                subChildObject = subChildrenArray.getJSONObject(m);
                                                int sub_child_topicId = subChildObject.getInt("TopicId");
                                                String sub_child_name = subChildObject.getString("Name");
                                                int sub_child_pageNo = subChildObject.getInt("PageNo");
                                                int sub_child_parentId = subChildObject.getInt("ParentId");
                                                int sub_child_fileId = subChildObject.getInt("FileId");
                                                String sub_child_file = childObject.getString("File");

                                                sub_childrenModel.topicId = sub_child_topicId;
                                                sub_childrenModel.name = sub_child_name;
                                                sub_childrenModel.pageNo = sub_child_pageNo;
                                                sub_childrenModel.parentId = sub_child_parentId;
                                                sub_childrenModel.fileId = sub_child_fileId;
                                                sub_childrenModel.file = sub_child_file;
                                                sub_childrenModel.save();
                                            } else {
                                                subChildObject = subChildrenArray.getJSONObject(m);
                                                tocSubChildrenModel = new TocSubChildrenModel();
                                                int sub_child_topicId = subChildObject.getInt("TopicId");
                                                String sub_child_name = subChildObject.getString("Name");
                                                int sub_child_pageNo = subChildObject.getInt("PageNo");
                                                int sub_child_parentId = subChildObject.getInt("ParentId");
                                                int sub_child_fileId = subChildObject.getInt("FileId");
                                                String sub_child_file = childObject.getString("File");

                                                tocSubChildrenModel.topicId = sub_child_topicId;
                                                tocSubChildrenModel.name = sub_child_name;
                                                tocSubChildrenModel.pageNo = sub_child_pageNo;
                                                tocSubChildrenModel.parentId = sub_child_parentId;
                                                tocSubChildrenModel.fileId = sub_child_fileId;
                                                tocSubChildrenModel.file = sub_child_file;
                                                tocSubChildrenModel.save();
                                            }
                                        }
                                        tocChildrenModel.save();
                                    }
                                }
                                parentModel.save();
                            }
                        }

                    } catch (JSONException e) {
                        //progressDialog.hide();
                        hidepDialog();
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //progressDialog.hide();
            hidepDialog();
        }
    }
}
